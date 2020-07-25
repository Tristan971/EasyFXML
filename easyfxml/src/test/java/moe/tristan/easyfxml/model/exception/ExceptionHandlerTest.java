/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.model.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.junit.FxmlComponentTest;

public class ExceptionHandlerTest extends FxmlComponentTest {

    private String EXCEPTION_TEXT;
    private String EXCEPTION_TEXT_READABLE;
    private Exception EXCEPTION;
    private Pane ERR_PANE;
    private Pane ERR_PANE_READBLE;

    @Start
    public void start(final Stage stage) {
        this.EXCEPTION_TEXT = "SAMPLE EXCEPTION";
        this.EXCEPTION_TEXT_READABLE = "USER READABLE ERROR";
        this.EXCEPTION = new Exception(this.EXCEPTION_TEXT);
        this.ERR_PANE = new ExceptionHandler(this.EXCEPTION).asPane();
        this.ERR_PANE_READBLE = new ExceptionHandler(this.EXCEPTION).asPane(this.EXCEPTION_TEXT_READABLE);
    }

    @Test
    public void asPane() {
        final Label errLabel = (Label) this.ERR_PANE.getChildren().filtered(node -> node instanceof Label).get(0);
        assertThat(errLabel.getText()).isEqualTo(this.EXCEPTION.getMessage());
    }

    @Test
    public void asPane_with_user_readable() {
        final Label errLabel = (Label) this.ERR_PANE_READBLE.getChildren()
                                                            .filtered(node -> node instanceof Label)
                                                            .get(0);
        assertThat(errLabel.getText()).isEqualTo(this.EXCEPTION_TEXT_READABLE);
    }

    @Test
    public void displayExceptionPane() throws ExecutionException, InterruptedException, TimeoutException {
        final CompletionStage<Stage> asyncDisplayedStage = ExceptionHandler.displayExceptionPane(
            this.EXCEPTION_TEXT,
            this.EXCEPTION_TEXT_READABLE,
            this.EXCEPTION
        );

        final Stage errStage = asyncDisplayedStage.toCompletableFuture().get(5, TimeUnit.SECONDS);

        assertThat(errStage.isShowing()).isTrue();
    }

}

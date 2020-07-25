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

package moe.tristan.easyfxml.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.vavr.control.Try;

public class ResourcesTest {

    private static final String PATH_UTIL_TESTS_FOLDER = "PathUtilsTests" + File.separator;

    private static final String EXISTING_FILE_NAME = "file_that_exists";
    private static final String EXISTING_FILE_CONTENT = "file_that_exists_content";
    private static final String EXISTING_ANOTHER_FILE_NAME = "another_file_that_exists";
    private static final String NONEXISTING_FILE_NAME = "file_that_doesnt_exist";

    @Test
    public void path_of_existing_file() {
        final Try<Path> fileThatExists = Resources.getResourcePath(
            PATH_UTIL_TESTS_FOLDER + EXISTING_FILE_NAME
        );

        assertThat(fileThatExists.isSuccess()).isTrue();
        fileThatExists.mapTry(Files::readAllLines).onSuccess(
            content -> assertThat(content)
                .hasSize(1)
                .containsExactly(EXISTING_FILE_CONTENT)
        );
    }

    @Test
    public void getResourcePath_should_display_path_tried() {
        final Try<Path> fileThatDoesntExist = Resources.getResourcePath(
            PATH_UTIL_TESTS_FOLDER + NONEXISTING_FILE_NAME
        );

        assertThat(fileThatDoesntExist.isFailure()).isTrue();
        assertThat(fileThatDoesntExist.getCause().getMessage()).contains(PATH_UTIL_TESTS_FOLDER +
                                                                         NONEXISTING_FILE_NAME);
    }

    @Test
    public void getResourceURL_should_display_path_tried() {
        final Try<URL> fileThatDoesntExist = Resources.getResourceURL(
            PATH_UTIL_TESTS_FOLDER + NONEXISTING_FILE_NAME
        );

        assertThat(fileThatDoesntExist.isFailure()).isTrue();

        assertThat(fileThatDoesntExist.getCause().getMessage()).contains(
            PATH_UTIL_TESTS_FOLDER + NONEXISTING_FILE_NAME
        );
    }

    @Test
    public void listFiles_existing_folder() {
        final Try<Path> pathUtilsTestFolder = Resources.getResourcePath(PATH_UTIL_TESTS_FOLDER);
        assertThat(pathUtilsTestFolder.isSuccess()).isTrue();

        final Try<List<Path>> files = Resources.listFiles(pathUtilsTestFolder.get());
        assertThat(files.isSuccess()).isTrue();
        assertThat(files.get().size()).isEqualTo(2);

        final List<String> fileNames = files.get().stream()
                                            .map(Path::getFileName)
                                            .map(Path::toString)
                                            .collect(Collectors.toList());

        assertThat(fileNames).containsExactlyInAnyOrder(
            EXISTING_FILE_NAME,
            EXISTING_ANOTHER_FILE_NAME
        );
    }

}

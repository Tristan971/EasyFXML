package moe.tristan.easyfxml.util;

import io.vavr.control.Try;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class PathUtilsTest {

    private static final String PATH_UTIL_TESTS_FOLDER = "PathUtilsTests/";

    private static final String EXISTING_FILE_NAME = "file_that_exists";
    private static final String EXISTING_FILE_CONTENT = "file_that_exists_content";

    private static final String EXISTING_ANOTHER_FILE_NAME = "another_file_that_exists";

    private static final String NONEXISTING_FILE_NAME = "file_that_doesnt_exist";
    private static final String NONEXISTING_FOLDER = "NonExistingFolder/";

    @Test
    public void path_of_existing_file() {
        final Try<Path> fileThatExists = PathUtils.getPathForResource(
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
    public void path_of_nonexisting_file() {
        final Try<Path> fileThatDoesntExist = PathUtils.getPathForResource(
            PATH_UTIL_TESTS_FOLDER + NONEXISTING_FILE_NAME
        );

        assertThat(fileThatDoesntExist.isFailure()).isTrue();
    }

    @Test
    public void listFiles_existing_folder() {
        final Try<Path> pathUtilsTestFolder = PathUtils.getPathForResource(PATH_UTIL_TESTS_FOLDER);
        assertThat(pathUtilsTestFolder.isSuccess()).isTrue();

        final List<Path> files = PathUtils.listFiles(pathUtilsTestFolder.get());
        assertThat(files.size()).isEqualTo(2);

        final List<String> fileNames = files.stream()
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(Collectors.toList());

        assertThat(fileNames).containsExactlyInAnyOrder(
            EXISTING_FILE_NAME,
            EXISTING_ANOTHER_FILE_NAME
        );
    }
}

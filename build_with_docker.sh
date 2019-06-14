#!/usr/bin/env bash

function assert_is_zero() {
    local NAME=$1
    local VALUE=$2

    [[ ${VALUE} -eq 0 ]] && echo "$NAME... OK" || echo "$NAME... NOK! (expect 0 but got: ${VALUE})"
}

function verify_environment() {
    echo "--- Verify environment ---"

    docker ps 1> /dev/null
    assert_is_zero "Docker daemon" "$?"
}

function build_docker_image() {
    echo "--- BUILD DOCKER IMAGE ---"

    IMAGE=$(cat easyfxml-docker/docker-build.env | grep "IMAGE" | cut -d'=' -f2)
    TAG=$(cat easyfxml-docker/docker-build.env | grep "TAG" | cut -d'=' -f2)

    echo "Will build image ${IMAGE}:${TAG}"
    set -x
    docker build "easyfxml-docker" -t "${IMAGE}:${TAG}"
    set +x
    assert_is_zero "Docker build result" "$?"
}

function build_via_docker_image() {
    echo "--- BUILD AND TEST IN DOCKER IMAGE ---"

    local CANDIDATE_IMAGES=$(docker images | grep ${IMAGE} | grep ${TAG})
    assert_is_zero "Image available" "$?"

    printf "Candidate images:\n$CANDIDATE_IMAGES\n"

    (which cygpath)
    local MOUNTED_DIR=$([[ $? -eq 0 ]] && echo $(cygpath -aw .) || echo $(pwd))

    docker run -v "${MOUNTED_DIR}:/root/EasyFXML" -v "~/.m2:/root/.m2" -it ${IMAGE}:${TAG} -c "maven_clean_install /root/EasyFXML"
}

verify_environment
build_docker_image
build_via_docker_image

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

    IMAGE=$(grep "IMAGE" easyfxml-docker/docker-build.env | cut -d'=' -f2)
    TAG=$(grep "TAG" easyfxml-docker/docker-build.env | cut -d'=' -f2)

    echo "Will build image ${IMAGE}:${TAG}"
    set -x
    docker build "easyfxml-docker" -t "${IMAGE}:${TAG}"
    set +x
    assert_is_zero "Docker build result" "$?"
}

function build_via_docker_image() {
    echo "--- BUILD AND TEST IN DOCKER IMAGE ---"

    local CANDIDATE_IMAGES
    CANDIDATE_IMAGES=$(docker images | grep "${IMAGE}" | grep "${TAG}")
    assert_is_zero "Image available" "$?"

    printf "Candidate images:\n%s\n" "$CANDIDATE_IMAGES"

    local MOUNTED_DIR
    local M2_DIR
    if [ "$(command -v cygpath)" ]; then
        MOUNTED_DIR=$(cygpath -aw .)
        M2_DIR=$(cygpath -aw "$HOME/.m2")
    else
        MOUNTED_DIR=$(pwd)
        M2_DIR="$HOME/.m2"
    fi

    local IMAGE_PROJECT_DIR
    IMAGE_PROJECT_DIR="/root/EasyFXML"

    set -x
    docker run \
        -v "${MOUNTED_DIR}:${IMAGE_PROJECT_DIR}" \
        -v "$M2_DIR:/root/.m2" \
        -it "${IMAGE}":"${TAG}" \
        -c "maven_clean_install ${IMAGE_PROJECT_DIR}"
}

verify_environment
build_docker_image
build_via_docker_image

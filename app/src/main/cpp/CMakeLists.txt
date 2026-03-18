cmake_minimum_required(VERSION 3.22.1)

# Major version number is equal to taglib major version number
project(
    "glyphport-opus-metadata-util"
    VERSION 2.0.0
)

# TagLib Configuration
set(BUILD_SHARED_LIBS OFF CACHE BOOL "Build Shared Libraries" FORCE)
set(BUILD_TESTING OFF CACHE BOOL "Build Tests" FORCE)
set(BUILD_EXAMPLES OFF CACHE BOOL "Build Examples" FORCE)
set(NO_ITUNES_HACKS ON CACHE BOOL "Disable iTunes Hacks" FORCE)
set(ENABLE_STATIC_RUNTIME OFF CACHE BOOL "Static Runtime" FORCE)

# Add TagLib source
# Make sure to update library info for taglib when changing version!
include(FetchContent)
FetchContent_Declare(
    taglib
    GIT_REPOSITORY https://github.com/taglib/taglib.git
    GIT_TAG        v2.2.1
)
FetchContent_MakeAvailable(taglib)

# Include TagLib headers
# We need to manually include all subdirectories because TagLib's CMake configuration
# does not export include directories for in-tree builds (it expects installation).
# The "toolkit" directory is needed for tstring.h, tbytevector.h etc.
# The "flac" is needed for flacpicture.h
include_directories(
    ${taglib_SOURCE_DIR}/taglib
    ${taglib_SOURCE_DIR}/taglib/toolkit
#    ${taglib_SOURCE_DIR}/taglib/asf
#    ${taglib_SOURCE_DIR}/taglib/mpeg
    ${taglib_SOURCE_DIR}/taglib/ogg
    ${taglib_SOURCE_DIR}/taglib/flac
#    ${taglib_SOURCE_DIR}/taglib/mpc
#    ${taglib_SOURCE_DIR}/taglib/mp4
#    ${taglib_SOURCE_DIR}/taglib/mod
#    ${taglib_SOURCE_DIR}/taglib/ape
#    ${taglib_SOURCE_DIR}/taglib/wavpack
#    ${taglib_SOURCE_DIR}/taglib/trueaudio
#    ${taglib_SOURCE_DIR}/taglib/riff
#    ${taglib_SOURCE_DIR}/taglib/riff/aiff
#    ${taglib_SOURCE_DIR}/taglib/riff/wav
)

# Include generated headers (taglib_config.h)
include_directories(${taglib_BINARY_DIR}/taglib)

# Create our JNI library
add_library(libopus-metadata-util SHARED libopus-metadata-util.cpp)

# Link against Android log library and TagLib
find_library(z-lib z)

target_link_libraries(libopus-metadata-util
        android
        log
        tag
        ${z-lib})

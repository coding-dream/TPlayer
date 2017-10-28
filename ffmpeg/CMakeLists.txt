cmake_minimum_required(VERSION 3.4.1)

#源码树的顶层路径。
set(distribution_DIR ${PROJECT_SOURCE_DIR}/src/main/jniLibs)

# 查看gradle Console 调试信息 PROJECT_SOURCE_DIR => E:/Codes/android_code/TPlayer/ffmpeg
message(STATUS "========> the PROJECT_SOURCE_DIR is : ${PROJECT_SOURCE_DIR}")
message(STATUS "========> the PROJECT_BINARY_DIR is : ${PROJECT_BINARY_DIR}")
message(STATUS "========> the cmakeList.txt PATH is : ${CMAKE_CURRENT_SOURCE_DIR}")
message(STATUS "========> distribution_DIR is : ${distribution_DIR}")

#判断编译器类型,如果是gcc编译器,则在编译选项中加入c++11支持
if(CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
    message(STATUS "optional:-std=c++11")
endif(CMAKE_COMPILER_IS_GNUCXX)

#支持-std=gnu++11
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 个人库
add_library( # Sets the name of the library.
             less

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             )
# 系统库
find_library( # Sets the name of the path variable.
              log-lib
              log )

find_library( # Sets the name of the path variable.
              z-lib
              z )

find_library( # Sets the name of the path variable.
              android-lib
              android )

# 第三方库
add_library(avcodec-57 SHARED IMPORTED)
set_target_properties(avcodec-57 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libavcodec-57.so)

add_library(avfilter-6 SHARED IMPORTED)
set_target_properties(avfilter-6 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libavfilter-6.so)

add_library(avformat-57 SHARED IMPORTED)
set_target_properties(avformat-57 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libavformat-57.so)

add_library(avutil-55 SHARED IMPORTED)
set_target_properties(avutil-55 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libavutil-55.so)

add_library(swresample-2 SHARED IMPORTED)
set_target_properties(swresample-2 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libswresample-2.so)

add_library(swscale-4 SHARED IMPORTED)
set_target_properties(swscale-4 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libswscale-4.so)

add_library(avdevice-57 SHARED IMPORTED)
set_target_properties(avdevice-57 PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libavdevice-57.so)

include_directories(${PROJECT_SOURCE_DIR}/src/main/jniLibs/include)
target_link_libraries(
                       less avcodec-57 avfilter-6 avformat-57 avutil-55 swresample-2 swscale-4 avdevice-57

                       ${log-lib}
                       ${z-lib}
                       ${android-lib}
                       )
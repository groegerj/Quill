TOP_LOCAL_PATH:= $(call my-dir)

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := Quill

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_JNI_SHARED_LIBRARIES := hpdf
LOCAL_STATIC_JAVA_LIBRARIES := org.apache.http.legacy
LOCAL_REQUIRED_MODULES := hpdf

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))


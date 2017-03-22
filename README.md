# Quill - Handwriting note-taking app for Android

Forked from https://code.google.com/archive/p/android-quill

The original code by Volker Braun has not been updated since 2013.
Still one of the best apps for Android, but a few things should
be updated.

## Changes

So far, the app has been made compatible with the AOSP build system,
i.e. made it compile as part of the Android sources in the process of creating a custom ROM.

Originally, I had written some scripts to convert the app's source tree
to a package compatible with AOSP.
These scripts were located in my repo Quill.AOSP,
which I am going to remove as this fork is a better way to achieve the same goal.

Compiling has been tested along with the source code tree of Cyanogenmod 13 as well as LineageOS 14.1.

more to come

## Dependencies

Quill depends on the libraries Ambilwarna and android-file-picker, which
I have included as part of the source tree.

### Ambilwarna

https://github.com/yukuku/ambilwarna

development idle, last substantial commit in 2015.

### android-file-picker

https://github.com/msoftware/android-file-picker

development idle, last commit in 2012.

### Further Dependencies

TODO


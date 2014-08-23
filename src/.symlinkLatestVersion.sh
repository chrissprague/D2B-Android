#!/bin/bash
LATEST=`find Versions/ -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -f2- -d" "`
rm "D2B-Android.apk"
ln -P $LATEST D2B-Android.apk
chmod o+rx D2B-Android.apk
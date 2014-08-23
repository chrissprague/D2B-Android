#!/bin/bash
LATEST=`find Versions/ -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -f2- -d" "`
echo "$LATEST" >> "cron.log"
rm "D2B-Android.apk"
ln -P $LATEST D2B-Android.apk
chmod o+rx D2B-Android.apk
echo $(date) >> "cron.log"
echo "Daily link success" >> "cron.log"
echo "" >> "cron.log"


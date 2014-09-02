#!/bin/bash
cd ..
LATEST=`find Versions/ -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -f2- -d" "`
echo "$LATEST" >> "symlinkScripts/cron.log"
rm "D2B-Android.apk"
ln -P $LATEST D2B-Android.apk
chmod o+rx D2B-Android.apk
echo $(date) >> "symlinkScripts/cron.log"
echo "Daily link success" >> "symlinkScripts/cron.log"
echo "" >> "symlinkScripts/cron.log"

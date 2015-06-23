#!/bin/sh
#
#	 make-jar-file
#

if [ ! -d tmp ]; then 
	mkdir tmp
else 
#	shouldn't we ask to do that?
	rm -drf tmp/*
fi

javac -d tmp -sourcepath src src/sh/luka/gui/*.java
jar cvfm jimageviewer.jar MANIFEST-ADD.MF -C tmp .

# rm -drf tmp

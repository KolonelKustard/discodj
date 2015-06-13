#!/bin/sh

set -e

VERSION=$1
WORKING_DIR=.
DEBS_LOCAL=$WORKING_DIR/debs
GHPAGES_LOCAL=$WORKING_DIR/discodj-gh-pages

WAR_DEB=discodj-debian-war-$VERSION.deb
PI_DEB=discodj-debian-pi-$VERSION.deb

if [ ! -d "$DEBS_LOCAL" ]; then
  mkdir $DEBS_LOCAL
fi

#wget --output-document=$DEBS_LOCAL/$WAR_DEB https://oss.sonatype.org/content/repositories/releases/com/totalchange/discodj/discodj-debian-war/$VERSION/$WAR_DEB
#wget --output-document=$DEBS_LOCAL/$PI_DEB https://oss.sonatype.org/content/repositories/releases/com/totalchange/discodj/discodj-debian-war/$VERSION/$PI_DEB

wget --output-document=$DEBS_LOCAL/$WAR_DEB https://oss.sonatype.org/content/repositories/snapshots/com/totalchange/discodj/discodj-debian-war/1.0.0-SNAPSHOT/discodj-debian-war-1.0.0-20150613.135638-1.deb
wget --output-document=$DEBS_LOCAL/$PI_DEB https://oss.sonatype.org/content/repositories/snapshots/com/totalchange/discodj/discodj-debian-pi/1.0.0-SNAPSHOT/discodj-debian-pi-1.0.0-20150613.140400-1.deb

if [ -d "$GHPAGES_LOCAL" ]; then
  (cd $GHPAGES_LOCAL && git pull origin gh-pages)
else
  git clone -b gh-pages --single-branch https://github.com/KolonelKustard/discodj $GHPAGES_LOCAL
fi

reprepro -b $GHPAGES_LOCAL/apt-repo includedeb discodj $DEBS_LOCAL/$WAR_DEB
reprepro -b $GHPAGES_LOCAL/apt-repo includedeb discodj $DEBS_LOCAL/$PI_DEB

(cd $GHPAGES_LOCAL/apt-repo && git add .)
(cd $GHPAGES_LOCAL && git commit -m "Deploying APT deb's for version $VERSION")
(cd $GHPAGES_LOCAL && git push origin gh-pages)

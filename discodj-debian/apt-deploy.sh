#!/bin/sh

set -e

VERSION=$1
WORKING_DIR=.
DEBS_LOCAL=$WORKING_DIR/debs
GHPAGES_LOCAL=$WORKING_DIR/discodj-gh-pages

DEB=discodj-debian-$VERSION.deb

if [ ! -d "$DEBS_LOCAL" ]; then
  mkdir $DEBS_LOCAL
fi

#wget --output-document=$DEBS_LOCAL/$DEB https://oss.sonatype.org/content/repositories/releases/com/totalchange/discodj/discodj-debian/$VERSION/$DEB

wget --output-document=$DEBS_LOCAL/$DEB https://oss.sonatype.org/content/repositories/snapshots/com/totalchange/discodj/discodj-debian/1.0.0-SNAPSHOT/discodj-debian-1.0.0-20150614.111751-3.deb

if [ -d "$GHPAGES_LOCAL" ]; then
  (cd $GHPAGES_LOCAL && git pull origin gh-pages)
else
  git clone -b gh-pages --single-branch https://github.com/KolonelKustard/discodj $GHPAGES_LOCAL
fi

reprepro -b $GHPAGES_LOCAL/apt-repo includedeb discodj $DEBS_LOCAL/$DEB

(cd $GHPAGES_LOCAL/apt-repo && git add .)
(cd $GHPAGES_LOCAL && git commit -m "Deploying APT deb for version $VERSION")
(cd $GHPAGES_LOCAL && git push origin gh-pages)

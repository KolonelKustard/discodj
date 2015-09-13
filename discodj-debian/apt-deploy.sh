#!/bin/bash

set -e

VERSION=${1/-/\~}
WORKING_DIR=.
GIT_URL=git@github.com:KolonelKustard/discodj.git
GHPAGES_LOCAL=$WORKING_DIR/discodj-gh-pages
DEB=/vagrant/target/discodj-debian\_$VERSION\_all.deb

if [[ -f $DEB ]] ; then
  echo Starting deploy of $DEB to apt repo
else
  echo $DEB not found - aborting deploy to apt repo
  exit 1
fi

if [ -d "$GHPAGES_LOCAL" ]; then
  echo Updating $GHPAGES_LOCAL
  (cd $GHPAGES_LOCAL && git pull origin gh-pages)
else
  echo Cloning gh-pages from $GIT_URL to $GHPAGES_LOCAL
  git clone -b gh-pages --single-branch --depth=1 $GIT_URL $GHPAGES_LOCAL
fi

echo Removing pre-existing version\(s\) from apt repo
reprepro -b $GHPAGES_LOCAL/apt-repo remove discodj discodj

echo Adding $DEB to apt repo
reprepro -b $GHPAGES_LOCAL/apt-repo includedeb discodj $DEB

echo Pushing changes to gh-pages
(cd $GHPAGES_LOCAL/apt-repo && git add .)
(cd $GHPAGES_LOCAL && git commit -m "Deploying APT deb for version $VERSION" --author="Maven Deploy <noreply@totalchange.com>")
(cd $GHPAGES_LOCAL && git push origin gh-pages)

#!/bin/sh

GHPAGES_LOCAL=./discodj-gh-pages

if [ -d "$GHPAGES_LOCAL" ]; then
  (cd $GHPAGES_LOCAL && git pull origin gh-pages)
else
  git clone -b gh-pages --single-branch https://github.com/KolonelKustard/discodj $GHPAGES_LOCAL
fi

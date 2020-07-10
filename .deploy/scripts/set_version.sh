#!/usr/bin/env bash

if [[ "$#" != "1" ]]; then
  echo "Usage: set_version.sh <version>"
fi

version="$1"
if [[ "$version" =~ ^(?:[0-9]+\.[0-9]+\.[0-9]+)|(?:1.0(-.+)*-SNAPSHOT)$ ]]; then
  echo "Tag must use semver or snapshot versioning (invalid: $version)"
  exit 1
fi

current_version="$(mvn -q --non-recursive org.codehaus.mojo:exec-maven-plugin:1.6.0:exec -Dexec.executable="echo" -Dexec.args='${project.version}')"
if [[ "$version" == "$current_version" ]]; then
  echo "Maven version matches tag version. Skipping version bump."
  exit
fi

echo "Version changed. Bumping Maven version to '$version'"
mvn -q --non-recursive versions:set -DnewVersion="$version"

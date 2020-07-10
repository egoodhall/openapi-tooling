#!/usr/bin/env bash
branch="${GITHUB_REF#refs/heads/}"
if [[ "$branch" == "master" ]];then
  echo "1.0-SNAPSHOT"
else
  echo "1.0-$branch-SNAPSHOT"
fi

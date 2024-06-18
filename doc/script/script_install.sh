#!/bin/bash

rm -rf SAE_hirtz_mellano_naigeon_reveillard

git clone git@github.com:GregoireHirtz/SAE-ProgAppRep.git

mv SAE-ProgAppRep SAE_hirtz_mellano_naigeon_reveillard

cd SAE_hirtz_mellano_naigeon_reveillard

mkdir jar

cd jar

wget https://github.com/GregoireHirtz/SAE-ProgAppRep/releases/download/v1/api.jar
wget https://github.com/GregoireHirtz/SAE-ProgAppRep/releases/download/v1/rmi.jar

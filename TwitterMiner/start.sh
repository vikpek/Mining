#!/bin/bash

until ant run;do
    echo "twittermood crashed with exit code $?.  Respawning... " >&2
		    sleep 1
				done

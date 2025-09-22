#!/bin/bash

#ssh -i "OPTICOMS-TR-NMS.pem" ec2-user@ec52-59-117-254.eu-central-1.compute.amazonaws.com

#scp -i OPTICOMS-TR-NMS.pem nms-backend-service-dev.sh ec2-user@ec52-59-117-254.eu-central-1.compute.amazonaws.com:~
scp -i OPTICOMS-TR-NMS.pem target/nms-lite-0.0.1-SNAPSHOT.jar ec2-user@ec2-52-59-117-254.eu-central-1.compute.amazonaws.com:~
#scp -i OPTICOMS-TR-NMS.pem src/main/resources/application-dev.yml ec2-user@ec52-59-117-254.eu-central-1.compute.amazonaws.com:~



#scp -i OPTICOMS-TR-NMS.pem KOPYALANCAK_DOSYA ec2-user@ec52-59-117-254.eu-central-1.compute.amazonaws.com:~
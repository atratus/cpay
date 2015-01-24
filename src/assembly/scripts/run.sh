#!/bin/bash

java -Dcpay.port=8085 -Ddb.url=jdbc:h2:~/cpay_host/cpay_db -cp .:"lib/*" trsit.cpay.CPay

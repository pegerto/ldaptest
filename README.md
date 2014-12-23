ldaptest
========

LDAP Search implementation to debug java application security without install ldap tools at server command line.

Example
=======

>java -jar daptest-1.0.jar -s ldap://ldap.forumsys.com -u  cn=read-only-admin,dc=example,dc=com  
 -p  password  -d dc=example,dc=com -f "(&(sn=Gauss)(objectClass=person))"


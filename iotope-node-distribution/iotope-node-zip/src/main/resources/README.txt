
									IOTOPE:node
									${project.version}
									Dry Fish Release


***************************************************************************
* WARNING   									                  WARNING *
* WARNING   This first release is a PROOF OF CONCEPT, so thread   WARNING *
* WARNING   it as such. Follow us on twitter at @IOTOPE!!!        WARNING * 
* WARNING                                                         WARNING *
***************************************************************************

INTODUCTION
===========
IOTOPE:node is an NFC terminal software package that can be configured to 
do all kind of stuff. In this first stage it's more like a package to let
developers explore the possibilities of NFC on a stand alone PC or Mac.

Although IOTOPE:node works now standalone it's not designed to stay that 
way. It will be a node within the IOTOPE ecosystem and will talk to the 
server. 

I'll invite you to vent your ideas on the google groups at:
https://groups.google.com/forum/#!forum/iotope


REQUIREMENTS
============

NFC Reader
----------
In this first release we only support the Touchatag (or equivalent) NFC 
reader. So an ACS122U reader will do but is missing a SAM card containing
the serial number of the reader. So I advice to grab a hold of a 
Touchatag reader.
- http://www.acs.com.hk/index.php?pid=product&prod_sections=0&id=ACR122U
- http://store.touchatag.com/acatalog/touchatag_starter_pack.html 
 
Reader Drivers
--------------
Don't forget to install the drivers of the NFC reader
- Windows 32 bit: http://www.acs.com.hk/drivers/eng/ACR122USAM_MSI_Winx86_1120_P.zip
- Windows 64 bit: http://www.acs.com.hk/drivers/eng/ACR122USAM_MSI_Winx64_1120_P.zip
(Mac and Linux still need to be tested)

Java Developers Kit
-------------------
Java Developers Kit version 1.6
http://www.oracle.com/technetwork/java/javase/downloads/index.html
The release in only tested on version 1.6


GETTING STATED
==============
We have 3 scenario's we've worked out that you can use this release of 
IOTOPE:node for.

1) Standalone Terminal
----------------------
This is the default active configuration. With this configuration IOTOPE:node
will work standalone without interacting with an external component.
 
Change the content of the following file "conf/active-config.properties" to:
ACTIVE=standalone

In this mode you can read the tag id from NFC tags and assign an application
to that tag (via the UI, on the tray icon use the "Node Console" menu, or goto
http://localhost:4242/ui/ ).

:node also supports reading the URL from an NDEF programmed tag (only NXP
Ultralight), and it will just to the programmed URL.
 

2) Touchatag Client
-------------------
If you have an account on the "Touchatag Community Site" you can configure
IOTOPE:node to act as a client. (http://www.touchatag.com).

Change the content of the following file "conf/active-config.properties" to:
ACTIVE=touchatag

Don't forget to add your credentials to the config file:
<application urn="urn:iotope.app:iotope.org:ttag.c12">
	<filter urn="urn:iotope.filter:iotope.org:legacy" type="include">
	</filter>
	<property name="user">user</property>
	<property name="password">PASSWORD</property>
</application>


It supports displaying text and URL's in the client.


3) WebHook
----------
A way to integrate with IOTOPE:node is to use the webhook. The webhook will
an HTTP POST to a configured endpoint with the content of the tag as a
JSON object.

Change the content of the following file "conf/active-config.properties" to:
ACTIVE=webhook

And change the configurethe URL by changing the property:
<application urn="urn:iotope.app:iotope.org:webhook">
	<property name="url">http://localhost:8811/iotopehook</property>
</application>


You can respond back with a JSON object that can active another object on the
pipeline. We supply examples in Phyton en Ruby in the examples folder.


KNOWN ISSUES
============
See the ISSUES.txt file


ROADMAP
=======
For now the ROADMAP for IOTOPE:node is just a bunch of ideas. Next step in the
IOTOPE ecosystem will be the server where IOTOPE:node can connect to. So the 
server is the next component that well 


RESOURCES
=========

Discuss IOTOPE in our Google groups
  https://groups.google.com/forum/#!forum/iotope
  
Follow us on Twitter
  @IOTOPE

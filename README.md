# LatencyAnalysis
LatencyAnalysis is a Maven Java project, and this project uses AWS SDK for Java to gather AWS service activity logs from
AWS CloudWatch Log. The program takes IoTSensor's generated text file to gather
corresponding AWS CloudWatch Log's service activity logs for each trial. Each collection of
logs is parsed to extract timestamps for desired events.

CloudWatch Logs does not offer latency information, so various latencies were calculated
locally based on the timestamps. Average of latencies were calculated in the end. 

Results will be printed to the console. If the user wishes to save the latency results, they could copy and paste
this text to a file. The program automatically saves logs acquired from CloudWatch for future
reference. 

# Requirement
1 text output from IOTSensor project

To run this project you need to set up AWS Credentials and region for development and import iotsensor project and latency project into Eclipse. 
For the setup on Windows 7, create an .aws folder at C:\User\USERNAME\.aws
Then, copy "credentials" file and "config" file (both are included in this zip) into .aws folder so that you have:
	 C:\User\USERNAME\.aws\credential
	 C:\User\USERNAME\.aws\config
For setting up credential in other operating systems, visit the following link for detail:
http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html

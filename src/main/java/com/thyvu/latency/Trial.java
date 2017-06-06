package com.thyvu.latency;

public class Trial {
	final static long DEFAULT_OFFSET = 90000L;
	
	public long srcTime, receivedTime, iotConnectTime, iotTopicFromSensorTime, iotRuleFoundTime, iotTime;
	public long dynamoActionSuccessTime, lambdaStartTime, lambdaSendToIotTime, lambdaSendToIotSuccessTime, 
			lambdaEndTime, iotTopicFromLambdaTime;
	public long lambdaSendToSnsTime, lambdaSendToSnsSuccessTime;
	public long cloudWatchLogFromLambdaTime, cloudWatchLogFromIotTime;
	
	// Approximate milliseconds of clock offset + CloudWatch Log latency to extract relevant CloudWatch Logs
	public long absoluteOffset; 	
	
	// Latencies in milliseconds
	public double rtt, roundTripNetwork, iotTopicFromSensor2IotTopicFromLambda, iotTopic2Rule, rule2db, db2lambda, 
			lambda2iotTopic,  lambda2sns, lambda2cw, iot2cw;
	
	public boolean calculatedLatencies=false;
	
	
	public long getAbsoluteOffset(){
		if(srcTime == 0 || iotTime == 0){
			System.out.println("srcTime and/or iotRuleTime is unknown.");
			return DEFAULT_OFFSET;
		} else if (absoluteOffset != 0){
			return absoluteOffset;
		}
		
		absoluteOffset = (long) (Math.ceil(Math.abs(iotTime - srcTime)/10000) * 10000 + 20000);
		
		return absoluteOffset;
	}
	
	
	
	/* 
	 * rtt = receivedTime - srcTime
	 * iotFromSensor2IotTopicFromLambda = iotTopicFromLambdaTime - iotTopicFromSensorTime
	 * roundTripNetwork = rtt - iotFromSensor2IotTopicFromLambda
	 * iotTopic2Rule = iotRuleTime - iotTopicFromSensorTime
	 * rule2db = dynamoActionSuccessTime - iotRuleTime
	 * db2lambda = lambdaStartTime - dynamoActionSucessTime
	 * lambda2iotPub = iotPublishFromLambda - lambdaSendTime
	 * lambda2cw = cloudWatchLogTime -lambdaEndTime
	 * 
	 * */
	
	public void calculateLatencies(){
		rtt = (int) (receivedTime - srcTime);
		
		iotTopicFromSensor2IotTopicFromLambda = (int) (iotTopicFromLambdaTime - iotTopicFromSensorTime);
				
		roundTripNetwork =  rtt -  (iotTopicFromSensor2IotTopicFromLambda);

		
		iotTopic2Rule = (int) ( iotRuleFoundTime  - iotTopicFromSensorTime);

		rule2db =(int) ( dynamoActionSuccessTime - iotRuleFoundTime);
		
		db2lambda = (int) (lambdaStartTime - dynamoActionSuccessTime);
		
		lambda2sns = (int) (lambdaSendToSnsSuccessTime - lambdaSendToSnsTime);
		
		lambda2iotTopic = (int) (iotTopicFromLambdaTime - lambdaSendToIotTime);
		
		lambda2cw = (int) (cloudWatchLogFromLambdaTime -lambdaEndTime);
		
		iot2cw = (int) (cloudWatchLogFromIotTime - iotTopicFromLambdaTime);
		
		
		
		
		
		calculatedLatencies = true;
	}
	
	public void printTimestamps(){
		long n = 1496270000000L;
		System.out.println("*****Timestamps in milliseconds since epoch*****");
		System.out.println("note: receivedTime and srcTime are based on sensor's clock."
				+ "\nOther times are based on Amazon AWS clock");
		System.out.println(  receivedTime  + " receivedTime");
		System.out.println(  srcTime   + " srcTime");
		System.out.println(  iotTime  +" iotTime" );
		System.out.println(  iotTopicFromSensorTime  +" iotTopicFromSensorTime"  );
		System.out.println( iotRuleFoundTime   +" iotRuleFoundTime");
		System.out.println(  dynamoActionSuccessTime   +" dynamoActionSuccessTime");
		System.out.println( lambdaStartTime   +" lambdaStartTime");
		System.out.println( lambdaSendToSnsTime  + " lambdaSendToSnsTime");
		System.out.println( lambdaSendToIotTime    +" lambdaSendToIotTime" );
		System.out.println( iotTopicFromLambdaTime   +" iotTopicFromLambdaTime");
		System.out.println( lambdaEndTime   +" lambdaEndTime");
		System.out.println( cloudWatchLogFromLambdaTime   +" cloudWatchLogTime" );
		
		
	}
	
	public void printLatencies(){
		if (!calculatedLatencies){
			calculateLatencies();
		}
		
		System.out.println("*****Latencies in milliseconds*****");
		System.out.printf("Total Round Trip: %.1f\n", rtt);
		System.out.printf("IoT Topic (from sensor) to IoT Topic (from lambda): %.1f\n", iotTopicFromSensor2IotTopicFromLambda);
		System.out.printf("Total Round Trip Network:  %.1f\n" , roundTripNetwork);
		System.out.printf("IoT Topic to IoT Rule:  %.1f\n" , iotTopic2Rule);
		System.out.printf("IoT Rule to DynamoDB:  %.1f\n" , rule2db );
		System.out.printf("DynamoDB to Lambda:  %.1f\n" , db2lambda );
		System.out.printf("Lambda to IoT Topic:  %.1f\n" , lambda2iotTopic);
		System.out.printf("Lambda to SNS:  %.1f\n" , lambda2sns);
		System.out.printf("Lambda to CloudWatch Log:  %.1f\n", lambda2cw);
		System.out.printf("IoT to CloudWatch Log:  %.1f\n", iot2cw);
		
		
	}
	
	
}

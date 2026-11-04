package com.example.shop.service;
public final class CardRules {
 private CardRules(){}
 public static boolean validLuhn(String number){if(number==null || !number.matches("[0-9]{12,19}"))return false;int sum=0;boolean doubleDigit=false;
  for(int i=number.length()-1;i>=0;i--){int n=number.charAt(i)-'0';if(doubleDigit){n*=2;if(n>9)n-=9;}sum+=n;doubleDigit=!doubleDigit;}return sum%10==0;
 }
 public static String scenario(String card){return switch(card){case "4111111111111111"->"SUCCESS";case "4000000000000002"->"INSUFFICIENT_FUNDS";case "4000000000000069"->"NETWORK_ERROR";case "4000000000000077"->"FRAUD_SUSPECTED";default->java.util.concurrent.ThreadLocalRandom.current().nextDouble()<0.9?"SUCCESS":"RANDOM_DECLINE";};}
}

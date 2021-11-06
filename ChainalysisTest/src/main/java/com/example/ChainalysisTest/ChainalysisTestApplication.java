
package com.example.ChainalysisTest;
import java.util.*;
import java.net.*;
import java.io.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
              
@SpringBootApplication
@RestController
@CrossOrigin
public class ChainalysisTestApplication {

public static void main(String[] args) {
    SpringApplication.run(ChainalysisTestApplication.class, args);
}
    @GetMapping("/hello")
	public String hello() {
    	return String.format("Hello!");
	}

  
	private static Double priceRequestCoinbase(String url) throws Exception{    
        //make the actual HTTP request to Coinbase servers and returns a Double of the price
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputline = in.readLine();   //the returned data is always a single line

        //parse the data and store as a Double
        String[] splitResult = inputline.split(":");
        String[] splitAnswer = splitResult[4].split("\"");
        String valueAsString = splitAnswer[1];
        Double currentValue = Double.valueOf(valueAsString);

        in.close();

        return currentValue;

    }

    private static Map<String, Double> getCoinbasePrices(){   
        //makes http requests to Coinbase api and returns a map of the prices
        
        // Coinbase api requests already take into account the 1% trading fee,
        // so the values returned by the request are the true buy and sell prices.
        String btcBuy = "https://api.coinbase.com/v2/prices/BTC-USD/buy";
        String btcSell = "https://api.coinbase.com/v2/prices/BTC-USD/sell";
        String ethBuy = "https://api.coinbase.com/v2/prices/ETH-USD/buy";
        String ethSell = "https://api.coinbase.com/v2/prices/ETH-USD/sell";


        Map<String, Double> coinbasePrices = new HashMap<String, Double>();


        try{
            coinbasePrices.put("btcBuy", priceRequestCoinbase(btcBuy));
            coinbasePrices.put("btcSell", priceRequestCoinbase(btcSell));
            coinbasePrices.put("ethBuy", priceRequestCoinbase(ethBuy));
            coinbasePrices.put("ethSell", priceRequestCoinbase(ethSell));

       } catch (Exception e){
           e.printStackTrace();
       }

       /*       //print statements for testing
        System.out.println("Coinbase Prices");
        System.out.println("BTC buy: " + coinbasePrices.get("btcBuy"));
        System.out.println("BTC sell: "+ coinbasePrices.get("btcSell"));
        System.out.println("ETH buy: " + coinbasePrices.get("ethBuy"));
        System.out.println("ETH sell: " + coinbasePrices.get("ethSell"));
*/
       return coinbasePrices;
    }

    private static Double priceRequestBinance(String url) throws Exception{ 
        //make the actual HTTP request to Binance servers and returns a Double of the price

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputline = in.readLine();   //return data is always a single line
        
        //parse the data and store as a Double
        String[] splitResult = inputline.split(":");
        String[] splitAnswer = splitResult[2].split("\"");
        String valueAsString = splitAnswer[1];
        Double currentValue = Double.valueOf(valueAsString);

        in.close();

        return currentValue;

    }

    private static Map<String, Double> getBinancePrices(){
    //makes http requests to Binance api and returns a map of the prices, accounting for trading fees


        // Binance api requests DO NOT include the trading fees, so they must be
        // calculated and added/subtracted from all requests to determine true
        // buy and sell prices.  
        String btcReq = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";
        String ethReq = "https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT";

        Double btcSpotPrice, ethSpotPrice;

        // According to Binance, they charge a 0.1% fee for spot trading and
        // 0.5% fee for instant trading.  These prices will assume that instant trading
        // will take place.
        Double currInstantTradeFee = 0.005;
        Map<String, Double> binancePrices = new HashMap<String, Double>();

        try{
            btcSpotPrice = priceRequestBinance(btcReq);
            ethSpotPrice = priceRequestBinance(ethReq);

            binancePrices.put("btcBuy", btcSpotPrice * (1 + currInstantTradeFee));
            binancePrices.put("btcSell", btcSpotPrice * (1 - currInstantTradeFee));
            binancePrices.put("ethBuy", ethSpotPrice * (1 + currInstantTradeFee));
            binancePrices.put("ethSell", ethSpotPrice * (1 - currInstantTradeFee));

            //this is to format the price to only 2 decimal places
            for (String key : binancePrices.keySet()){
                String temp = binancePrices.get(key).toString();
                String[] removeDecimal = temp.split("\\.");
                String decimalPoints = removeDecimal[1].substring(0, 2);
                String formattedString = removeDecimal[0] + "." + decimalPoints;
                Double currentValue = Double.valueOf(formattedString);
                
                binancePrices.put(key, currentValue);

            }

       } catch (Exception e){
           e.printStackTrace();
       }

/*       //print statements for testing
       System.out.println("Binance prices");
       System.out.println("BTC buy: " + binancePrices.get("btcBuy"));
       System.out.println("BTC sell: " + binancePrices.get("btcSell"));
       System.out.println("ETH buy: " + binancePrices.get("ethBuy"));
       System.out.println("ETH sell: " + binancePrices.get("ethSell"));
*/

       return binancePrices;
    }

    private static Map<String, String> findRecommendations(Map<String, Double> coinbaseMap, Map<String, Double> binanceMap){
        //takes two maps, one consisting of all of the Coinbase prices and one of all the Binance prices
        // returns a map that has from which exchange you should buy and/or sell BTC and ETH

        Set<String> keySet = coinbaseMap.keySet();
        Map<String, String> recommendations = new HashMap<String, String>();

        for (String key: keySet){
            Double coinbasePrice = coinbaseMap.get(key);
            Double binancePrice = binanceMap.get(key);

            if (key.contains("Buy")){   //recommend the smaller number
                if (coinbasePrice < binancePrice){
                    recommendations.put(key, " Coinbase");
                } else {
                    recommendations.put(key, " Binance");
                }
            } else {    //recomend the larger number
                if (coinbasePrice > binancePrice){
                    recommendations.put(key, " Coinbase");
                } else {
                    recommendations.put(key, " Binance");
                }
            }

        }
        /*  //print statements for testing
        System.out.println("Recommendations");
        for (String type: recommendations.keySet()){
            System.out.println(type + recommendations.get(type));
        }
        */

        return recommendations;
    }
    
    @GetMapping("/run")     //the url that the frontend calls to begin making requests for the prices
	public String run(){
        //starts the script that gathers all the prices
        //returns a String to the frontend that contains all the data 

        try{
            Map<String, Double> coinbasePrices = getCoinbasePrices();
            Map<String, Double> binancePrices = getBinancePrices();
            Map<String, String> recommendations = findRecommendations(coinbasePrices, binancePrices);

            String data = "";

            data = data + coinbasePrices.get("btcBuy").toString() + ", ";
            data = data + coinbasePrices.get("btcSell").toString() + ", ";
            data = data + coinbasePrices.get("ethBuy").toString() + ", ";
            data = data + coinbasePrices.get("ethSell").toString() + ", ";

            data = data + binancePrices.get("btcBuy").toString() + ", ";
            data = data + binancePrices.get("btcSell").toString() + ", ";
            data = data + binancePrices.get("ethBuy").toString() + ", ";
            data = data + binancePrices.get("ethSell").toString() + ", ";

            data = data + recommendations.get("btcBuy") + ", ";
            data = data + recommendations.get("btcSell") + ", ";
            data = data + recommendations.get("ethBuy") + ", ";
            data = data + recommendations.get("ethSell");

            return data;

        } catch (Exception e){
            e.printStackTrace();
        }  
		return "There was an error";
    }
}          
import React, { useState, useEffect } from "react";
import './App.css';
import Exchange from './Exchange'
import Recommendations from './Recommendations';
import axios from "axios";


function Middle(){
    const [data, setData] = useState([]);

    useEffect(() => {
      (async () => {
        try {     //calls the backend to start making the http requests to the exchanges
          const response = await axios.get("http://localhost:8080/run"); 
          const data = response.data;
          setData(data.split(","));
          console.log(data);
        } catch(err) {
          console.error(err);
        }
      })()
    })

    return (
      //this is there the data that was recieved from the backend
      //is implemented onto the webpage
        <div className = 'Middle'> 
           <Exchange name = "Coinbase" btcBuy = {data[0]} btcSell = {data[1]} ethBuy = {data[2]} ethSell = {data[3]}/>
           <Exchange name = "Binance" btcBuy = {data[4]} btcSell = {data[5]} ethBuy = {data[6]} ethSell = {data[7]}/>
           <Recommendations buyBTC = {data[8]} sellBTC = {data[9]} buyETH = {data[10]} sellETH = {data[11]}/>
        </div>

    );
};
export default Middle;
import React from 'react';
import './App.css';


function Exchange(props){


    return (
      //takes in all the inputs from the 
    <div className = "Exchange">
      <h1>{props.name}</h1>
      <p>BTC buy price: {props.btcBuy}</p>
      <p>BTC sell price: {props.btcSell}</p>
      <p>ETH buy price: {props.ethBuy}</p>
      <p>ETH sell price: {props.ethSell}</p>
      
    </div>
  );
};

export default Exchange;
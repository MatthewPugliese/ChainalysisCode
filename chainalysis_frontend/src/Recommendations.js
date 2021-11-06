import React from 'react';
import './App.css';

function Recommendations(props){

    return (
        //takes the data from the backend and puts the proper recommendations
        //onto the webpage
        <div className = 'Recommendations'>
            <h1>Recommendations</h1>
            <p>Buy BTC on: {props.buyBTC}</p>
            <p>Sell BTC on: {props.sellBTC}</p>
            <p>Buy ETH on: {props.buyETH}</p>
            <p>Sell ETH on: {props.sellETH}</p>
        </div>

    );
};
export default Recommendations;
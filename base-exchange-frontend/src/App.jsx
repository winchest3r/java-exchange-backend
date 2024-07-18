import { useState, useEffect } from 'react';

import services from './services';

import CurrencyList from './components/CurrencyList';
import ExchangeRateList from './components/ExchangeRateList';

const App = () => {
    const [currencies, setCurrencies] = useState([]);
    const [exchangeRates, setExchangeRates] = useState([]);

    useEffect(() => {
        services
            .getCurrencies()
            .then(data => setCurrencies(data));

    }, []);

    useEffect(() => {
        services
            .getExchangeRates()
            .then(data => setExchangeRates(data));
    }, []);

    return (
        <div>
            <h2>currencies</h2>
            <CurrencyList
                currencies={currencies}
                setCurrencies={setCurrencies}
            />
            <h2>exchange rates</h2>
            <ExchangeRateList
                exchangeRates={exchangeRates}
                setExchangeRates={setExchangeRates}
            />
        </div>
    );
};

export default App;
import { useState, useEffect } from 'react';

import services from './services';

import CurrencyList from './components/CurrencyList';
import ExchangeRateList from './components/ExchangeRateList';
import Notification from './components/Notification';
import ExchangeForm from './components/ExchangeForm';


const App = () => {
    const [currencies, setCurrencies] = useState([]);
    const [exchangeRates, setExchangeRates] = useState([]);
    const [note, setNote] = useState('');

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
            <Notification note={note} />
            <ExchangeForm
                exchangeRates={exchangeRates}
                currencies={currencies}
                setNote={setNote}
            />
            <hr />
            <h2>currencies</h2>
            <CurrencyList
                currencies={currencies}
                setCurrencies={setCurrencies}
                setNote={setNote}
            />
            <hr />
            <h2>exchange rates</h2>
            <ExchangeRateList
                exchangeRates={exchangeRates}
                setExchangeRates={setExchangeRates}
                setNote={setNote}
                currencies={currencies}
            />
        </div>
    );
};

export default App;
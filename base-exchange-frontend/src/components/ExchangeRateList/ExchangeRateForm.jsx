import { useState } from 'react';

import services from '../../services';

const ExchangeRateForm = ({ exchangeRates, setExchangeRates, setNote, currencies }) => {
    const [baseCode, setBaseCode] = useState('RUB');
    const [targetCode, setTargetCode] = useState('RUB');
    const [rate, setRate] = useState(1.0000);

    const submit = (event) => {
        event.preventDefault();

        const exchange = exchangeRates.find(e => 
            e.baseCurrency.code === baseCode
            && e.targetCurrency.code === targetCode);
        
        if (exchange) {
            services
                .updateExchangeRate(baseCode, targetCode, rate)
                .then(data => 
                    setExchangeRates(exchangeRates.map(
                        e => e.id === data.id ? data : e
                    ))
                )
                .catch(e => {
                    setNote(e.response.data.error);
                    setTimeout(() => setNote(''), 5000);
                });
        } else {
            services
                .addNewExchangeRate(baseCode, targetCode, rate)
                .then(data => setExchangeRates(exchangeRates.concat(data)))
                .catch(e => {
                    setNote(e.response.data.error);
                    setTimeout(() => setNote(''), 5000);
                })
        }
    }

    const checkRateExistence = (base, target) => {
        const exchange = exchangeRates.find(
            e => e.baseCurrency.code === base &&
                e.targetCurrency.code === target
        );
        setRate(exchange? exchange.rate : 1.0000);
    }

    const changeBaseCode = (event) => {
        checkRateExistence(event.target.value, targetCode);
        setBaseCode(event.target.value);
    }

    const changeTargetCode = (event) => {
        checkRateExistence(baseCode, event.target.value);
        setTargetCode(event.target.value);
    }

    return (
        <form onSubmit={submit}>
            <div>
                <select onChange={changeBaseCode}>
                    {currencies.map(c =>
                        <option key={c.id} value={c.code}>
                            {c.name}
                        </option>
                    )}
                </select>
            </div>
            <div>
                <select onChange={changeTargetCode}>
                    {currencies.map(c =>
                        <option key={c.id} value={c.code}>
                            {c.name}
                        </option>
                    )}
                </select>
            </div>
            <div>
                <input
                    type="number"
                    value={rate}
                    step="0.0001"
                    min="0"
                    onChange={e => setRate(e.target.value)}
                />
            </div>
            <button type="submit">add new exchange rate</button>
        </form>
    );
};

export default ExchangeRateForm;

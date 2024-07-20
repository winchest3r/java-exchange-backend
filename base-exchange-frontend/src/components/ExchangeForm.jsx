import { useState } from 'react';

import services from '../services';

const ExchangeForm = ({ exchangeRates, currencies, setNote }) => {
    const [baseCode, setBaseCode] = useState('RUB');
    const [targetCode, setTargetCode] = useState('RUB');
    const [amount, setAmount] = useState(100.0000);
    const [message, setMessage] = useState(
        'Use form to exchange currencies. Currencies can be converted from USD'
    );

    const submit = (event) => {
        event.preventDefault();

        services
            .exchange(baseCode, targetCode, amount)
            .then(data => setMessage(
                `${data.baseCurrency.name} converted to ${data.targetCurrency.name}: 
                ${data.amount}${data.baseCurrency.sign} = ${data.convertedAmount}${data.targetCurrency.sign}`
            ))
            .catch(e => {
                setNote(e.response.data.error);
                setTimeout(() => setNote(''), 5000);
            });
    }

    return (
        <div>
            <form onSubmit={submit}>
                <div>
                    <select onChange={e => setBaseCode(e.target.value)}>
                        {currencies.map(c =>
                            <option key={c.id} value={c.code}>
                                {c.name}
                            </option>
                        )}
                    </select>
                </div>
                <div>
                    <select onChange={e => setTargetCode(e.target.value)}>
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
                        value={amount}
                        step="0.01"
                        min="0"
                        onChange={e => setAmount(e.target.value)}
                    />
                </div>
                <button type="submit">convert</button>
            </form>
            <p>
                {message}
            </p>
        </div>
    );
};

export default ExchangeForm;

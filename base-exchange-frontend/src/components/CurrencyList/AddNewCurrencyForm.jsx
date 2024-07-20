import { useState } from 'react';

import services from '../../services';

const AddNewCurrencyForm = ({ currencies, setCurrencies, setNote }) => {
    const [name, setName] = useState('');
    const [code, setCode] = useState('');
    const [sign, setSign] = useState('');

    const submit = (event) => {
        event.preventDefault();

        services
            .addNewCurrency(name, code, sign)
            .then(data => setCurrencies(currencies.concat(data)))
            .catch(e => {
                setNote(e.response.data.error);
                setTimeout(() => setNote(''), 5000);
            });
        
        setName('');
        setCode('');
        setSign('');
    }

    return (
        <form onSubmit={submit}>
            <div>
                <input
                    placeholder="name"
                    value={name}
                    onChange={e => setName(e.target.value)}
                />
            </div>
            <div>
                <input
                    placeholder="code"
                    value={code}
                    onChange={e => setCode(e.target.value)}
                />
            </div>
            <div>
                <input
                    placeholder="sign"
                    value={sign}
                    onChange={e => setSign(e.target.value)}
                />
            </div>
            <button type="submit">add new currency</button>
        </form>
    );
}

export default AddNewCurrencyForm;
import { useState, useEffect } from 'react';

import services from './services';

const App = () => {
    const [currencies, setCurrencies] = useState([]);

    useEffect(() => {
        services
            .getCurrencies()
            .then(data => setCurrencies(data));

    }, [])

    return (
        <div>
            {currencies.map(c => 
                <p key={c.id}>{c.code}</p>
            )}
        </div>
    );
};

export default App;
const ExchangeRateList = ({ exchangeRates, setExchangeRates }) => {
    return (
        <div>
            <table>
                <thead>
                    <tr>
                        <th>Base</th>
                        <th>Target</th>
                        <th>Rate</th>
                    </tr>
                </thead>
                <tbody>
                    {exchangeRates.map(e =>
                        <tr key={e.id}>
                            <td>{e.baseCurrency.code}</td>
                            <td>{e.targetCurrency.code}</td>
                            <td>{e.rate}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default ExchangeRateList;

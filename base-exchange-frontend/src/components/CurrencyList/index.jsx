const CurrencyList = ({ currencies, setCurrencies }) => {
    return (
        <div>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Sign</th>
                    </tr>
                </thead>
                <tbody>
                    {currencies.map(c =>
                        <tr key={c.id}>
                            <td>{c.name}</td>
                            <td>{c.code}</td>
                            <td>{c.sign}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default CurrencyList;
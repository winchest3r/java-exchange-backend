import AddNewCurrencyForm from "./AddNewCurrencyForm";

const CurrencyList = ({ currencies, setCurrencies, setNote }) => {
    return (
        <div>
            <AddNewCurrencyForm
                currencies={currencies}
                setCurrencies={setCurrencies}
                setNote={setNote}
            />
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
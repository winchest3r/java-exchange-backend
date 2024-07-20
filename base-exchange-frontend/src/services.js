import axios from 'axios';

const apiUrl = import.meta.env.PROD ? '/' + import.meta.env.VITE_APP_NAME : '';

const currenciesBaseUrl = apiUrl + '/api/currencies';
const exchangeRatesBaseUrl = apiUrl + '/api/exchange-rates';
const exchangeBaseUrl = apiUrl + '/api/exchange';

const getCurrencies = async () => {
    const response = await axios.get(currenciesBaseUrl);
    return response.data;
};

const getExchangeRates = async () => {
    const response = await axios.get(exchangeRatesBaseUrl);
    return response.data;
};

const addNewCurrency = async (name, code, sign) => {
    const response = await axios.post(
        `${currenciesBaseUrl}?name=${name}&code=${code}&sign=${sign}`
    );
    return response.data;
}

const addNewExchangeRate = async (baseCode, targetCode, rate) => {
    const response = await axios.post(
        `${exchangeRatesBaseUrl}?baseCurrencyCode=${baseCode}&targetCurrencyCode=${targetCode}&rate=${rate}`
    );
    return response.data;
}

const updateExchangeRate = async (baseCode, targetCode, rate) => {
    const response = await axios.put(
        `${exchangeRatesBaseUrl}/${baseCode}${targetCode}?rate=${rate}`
    );
    return response.data;
}

const exchange = async (fromCode, toCode, amount) => {
    const response = await axios.get(
        `${exchangeBaseUrl}?from=${fromCode}&to=${toCode}&amount=${amount}`
    );
    return response.data;
}

export default {
    getCurrencies,
    getExchangeRates,
    addNewCurrency,
    addNewExchangeRate,
    updateExchangeRate,
    exchange,
};
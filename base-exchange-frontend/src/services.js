import axios from 'axios';

const apiUrl = import.meta.env.PROD ? '/' + import.meta.env.VITE_APP_NAME : '';

const currenciesBaseUrl = apiUrl + '/api/currencies';
const exchangeRatesBaseUrl = apiUrl + '/api/exchange-rates';

const getCurrencies = async () => {
    const response = await axios.get(currenciesBaseUrl);
    return response.data;
};

const getExchangeRates = async () => {
    const response = await axios.get(exchangeRatesBaseUrl);
    return response.data;
};

export default {
    getCurrencies,
    getExchangeRates,
};
# API

## Entities

### Stock

1. Id - unique identifier
2. Name - name shown to user
3. Type - category of stock (Bond, Share, etc.)
4. Price - current price

### PriceRecord

1. Id - unique identifier
2. StockId - stock which price is represented
3. Price - price at the moment of time
4. Timestamp - this moment of time

## Endpoints

1. CRUD + Search for Stocks
2. CRUD + Search for Price Records
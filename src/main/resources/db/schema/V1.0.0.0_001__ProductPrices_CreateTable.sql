CREATE TABLE PRODUCT_PRICES (
  id                    INTEGER                   NOT NULL,
  brandId               INTEGER                   NOT NULL,
  startDate             TIMESTAMP WITH TIME ZONE  NOT NULL,
  endDate               TIMESTAMP WITH TIME ZONE  NOT NULL,
  priceList             INTEGER                   NOT NULL,
  productId             INTEGER                   NOT NULL,
  priority              INTEGER                   NOT NULL,
  price                 DOUBLE                    NOT NULL,
  currencyCode          VARCHAR(3)                NOT NULL,

  CONSTRAINT PK_PRODUCT_PRICES PRIMARY KEY (id)
);
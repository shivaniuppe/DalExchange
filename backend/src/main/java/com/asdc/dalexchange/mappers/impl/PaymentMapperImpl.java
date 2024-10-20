package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.PaymentDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between Payment and PaymentDTO.
 */
@Component
@Slf4j
public class PaymentMapperImpl implements Mapper<Payment, PaymentDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the PaymentMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public PaymentMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        initializeMappings();
    }

    /**
     * Initializes custom mappings between Payment and PaymentDTO.
     */
    private void initializeMappings() {
        log.info("Initializing custom mappings between Payment and PaymentDTO.");
        TypeMap<Payment, PaymentDTO> propertyMapper = this.modelMapper.createTypeMap(Payment.class, PaymentDTO.class);
        // Custom mapping configurations can be added here if needed.
    }

    /**
     * Maps a Payment entity to a PaymentDTO.
     *
     * @param payment the Payment entity to map from.
     * @return the mapped PaymentDTO.
     */
    @Override
    public PaymentDTO mapTo(Payment payment) {
        log.info("Mapping Payment with id: {} to PaymentDTO.", payment.getPaymentId());
        PaymentDTO paymentDTO = this.modelMapper.map(payment, PaymentDTO.class);
        log.debug("Mapped PaymentDTO: {}", paymentDTO);
        return paymentDTO;
    }

    /**
     * Maps a PaymentDTO to a Payment entity.
     *
     * @param paymentDTO the PaymentDTO to map from.
     * @return the mapped Payment entity.
     */
    @Override
    public Payment mapFrom(PaymentDTO paymentDTO) {
        log.info("Mapping PaymentDTO with id: {} to Payment.", paymentDTO.getPaymentId());
        Payment payment = this.modelMapper.map(paymentDTO, Payment.class);
        log.debug("Mapped Payment: {}", payment);
        return payment;
    }
}

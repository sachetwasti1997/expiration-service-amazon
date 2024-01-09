package com.sachet.expirationserviceamazon.repository;

import com.sachet.expirationserviceamazon.modal.OrderCreatedEventModal;
import org.springframework.data.repository.CrudRepository;

public interface ExpirationRepository extends CrudRepository<OrderCreatedEventModal, String> {
}

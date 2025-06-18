package com.example.PEP3_Tingeso_Backend.services;

import com.example.PEP3_Tingeso_Backend.repositories.VoucherRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    @Mock
    VoucherRepository voucherRepository;

    @InjectMocks
    VoucherService voucherService;
}

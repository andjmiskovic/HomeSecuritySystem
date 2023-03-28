package com.team4.secureit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;

@Setter
@Getter
@Data
@AllArgsConstructor
public class IssuerData {
    private X500Name x500name;
    private PrivateKey privateKey;
}

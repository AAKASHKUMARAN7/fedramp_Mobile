# SUPPLY CHAIN RISK MANAGEMENT PLAN (SCRM)

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Regulatory Basis** | NIST SP 800-161 Rev. 1; NIST SP 800-53 Rev. 5: SR Family; EO 14028 |

---

## 1. PURPOSE

This Supply Chain Risk Management (SCRM) Plan identifies, assesses, and mitigates risks associated with the supply chain of the Vivo T2X 5G Mobile Information System. The plan addresses hardware, software, firmware, and service supply chains from manufacturing through operational lifecycle.

---

## 2. SUPPLY CHAIN OVERVIEW

### 2.1 Tier 1 Suppliers (Direct)

| # | Supplier | Country | Component | Risk Level | Critical? |
|---|---|---|---|---|---|
| SCR-001 | **Vivo (BBK Electronics)** | China | Device manufacturer (V2312) | **HIGH** | Yes |
| SCR-002 | **MediaTek** | Taiwan | SoC — Dimensity 6020 (MT6833) | **MODERATE** | Yes |
| SCR-003 | **Google LLC** | United States | Android 15 OS, Play Services, GMS | **LOW** | Yes |
| SCR-004 | **Reliance Jio** | India | Cellular network, SIM, baseband services | **LOW** | Yes |
| SCR-005 | **Trustonic** | United Kingdom | TEE OS (Kinibi), hardware keystore | **LOW** | Yes |

### 2.2 Tier 2 Suppliers (Indirect / Sub-component)

| # | Supplier | Country | Component | Risk Level |
|---|---|---|---|---|
| SCR-006 | ARM Holdings | United Kingdom | CPU architecture (ARMv8.2-A) | LOW |
| SCR-007 | Samsung / SK Hynix | South Korea | LPDDR4X RAM, UFS storage (estimated) | LOW |
| SCR-008 | Qualcomm | United States | Baseband modem (estimated 5G/LTE) | LOW |
| SCR-009 | Corning / AGC | US / Japan | Display glass (estimated) | LOW |
| SCR-010 | BOE / CSOT | China | LCD/AMOLED display panel | MODERATE |

### 2.3 Tier 3 Suppliers (Software / Services)

| # | Supplier | Country | Component | Risk Level |
|---|---|---|---|---|
| SCR-011 | Meta Platforms | United States | WhatsApp, Instagram | MODERATE |
| SCR-012 | Telegram FZ-LLC | Dubai (UAE) | Telegram | MODERATE |
| SCR-013 | State Bank of India | India | SBI YONO banking app | LOW |
| SCR-014 | NIC / MeitY (GoI) | India | DigiLocker, government apps | LOW |
| SCR-015 | Various developers | Multiple | 38 user-installed applications | MODERATE |

---

## 3. SUPPLY CHAIN RISK ASSESSMENT

### 3.1 Risk Assessment Methodology

Risks are assessed using the following criteria:

| Factor | Weight | Description |
|---|---|---|
| Country of Origin | 30% | Geopolitical risk, regulatory environment, data access laws |
| Supplier Trustworthiness | 25% | Track record, certifications, transparency |
| Component Criticality | 20% | Impact if supply chain is compromised |
| Alternative Availability | 15% | Ability to substitute supplier/component |
| Known Vulnerabilities | 10% | Historical CVEs, supply chain incidents |

### 3.2 Detailed Risk Analysis

#### SCR-001: Vivo (BBK Electronics) — HIGH RISK

| Attribute | Assessment |
|---|---|
| **Country** | China (People's Republic of) |
| **Parent Company** | BBK Electronics (also Oppo, OnePlus, Realme, iQOO) |
| **Risk Factors** | Chinese National Intelligence Law (2017) — mandates cooperation with state intelligence; potential firmware-level access; limited transparency in manufacturing; no FedRAMP certification; not on GSA approved products list |
| **Component** | Entire device hardware, FuntouchOS skin, pre-installed bloatware, bootloader, firmware |
| **Known Concerns** | Multiple Chinese smartphone manufacturers have faced scrutiny (Huawei ban, ZTE restrictions); Vivo not currently sanctioned but shares risk profile |
| **Mitigations** | 1. Verified Boot = green (no unauthorized firmware modification detected). 2. Bootloader locked. 3. Google Play Protect scans all apps. 4. SELinux Enforcing limits manufacturer backdoor capability. 5. Network monitoring for suspicious traffic. 6. Consider replacement with US/Allied OEM for Moderate/High data |
| **Residual Risk** | **HIGH** — Cannot fully mitigate OEM firmware-level risk |

#### SCR-002: MediaTek — MODERATE RISK

| Attribute | Assessment |
|---|---|
| **Country** | Taiwan (Republic of China) |
| **Risk Factors** | Chip design in Taiwan, fabrication partially in China (TSMC/other fabs); baseband firmware partially closed-source; historical CVEs in MediaTek chipsets |
| **Component** | System-on-Chip (Dimensity 6020 / MT6833), GPU, ISP, baseband |
| **Known Vulnerabilities** | MediaTek-su privilege escalation (2020, CVE-2020-0069), various DSP vulnerabilities |
| **Mitigations** | 1. Security patches applied via OTA. 2. SELinux limits privilege escalation. 3. Kernel 4.19.236+ includes MediaTek fixes. 4. ARM crypto extensions (AES, SHA) hardware-verified |
| **Residual Risk** | **MODERATE** — Patch cadence from Vivo lags behind MediaTek releases |

#### SCR-003: Google LLC — LOW RISK

| Attribute | Assessment |
|---|---|
| **Country** | United States |
| **Risk Factors** | Data collection concerns; large attack surface (Play Services); dependency on Google infrastructure |
| **Component** | Android 15 (AOSP + proprietary GMS), Play Store, Play Protect, Cloud Backup |
| **Certifications** | FedRAMP Authorized (Google Cloud), SOC 2 Type II, ISO 27001 |
| **Mitigations** | 1. Android open-source portions auditable. 2. Monthly security patch cycle. 3. Play Protect provides anti-malware. 4. Well-documented privacy controls. 5. US jurisdiction with legal process requirements |
| **Residual Risk** | **LOW** — Trusted supplier with FedRAMP authorization |

#### SCR-004: Reliance Jio — LOW RISK

| Attribute | Assessment |
|---|---|
| **Country** | India |
| **Risk Factors** | Carrier has access to communication metadata; Indian IT Act data access provisions |
| **Component** | Cellular connectivity, DNS, Internet gateway, SIM card |
| **Mitigations** | 1. VPN to encrypt traffic through carrier network. 2. DoH/DoT to protect DNS. 3. Legitimate carrier with regulatory oversight. 4. LTE/5G NR encryption at radio layer |
| **Residual Risk** | **LOW** — Standard carrier risk; VPN mitigates |

#### SCR-005: Trustonic — LOW RISK

| Attribute | Assessment |
|---|---|
| **Country** | United Kingdom |
| **Component** | Trusted Execution Environment (TEE), hardware keystore (Level 41) |
| **Certifications** | Common Criteria certified, GlobalPlatform TEE certified |
| **Mitigations** | 1. Hardware-isolated from Android OS. 2. Security evaluated by third parties. 3. Five Eyes ally jurisdiction |
| **Residual Risk** | **LOW** — Well-certified, hardware-isolated component |

---

### 3.3 Risk Summary Matrix

| Risk Level | Suppliers | Key Concern |
|---|---|---|
| **HIGH** | Vivo (SCR-001) | Chinese OEM with firmware-level access |
| **MODERATE** | MediaTek (SCR-002), Meta (SCR-011), Telegram (SCR-012), Various apps (SCR-015), BOE/CSOT (SCR-010) | Partial closed-source; data collection; varying trust |
| **LOW** | Google (SCR-003), Jio (SCR-004), Trustonic (SCR-005), ARM (SCR-006), Samsung/Hynix (SCR-007), Qualcomm (SCR-008), Corning/AGC (SCR-009), SBI (SCR-013), NIC/MeitY (SCR-014) | Trusted suppliers, certifications, allied nations |

---

## 4. SUPPLY CHAIN THREAT SCENARIOS

| # | Threat Scenario | Likelihood | Impact | Affected Suppliers |
|---|---|---|---|---|
| SCTH-001 | Pre-installed malware/spyware in firmware | Low-Moderate | Critical | SCR-001 (Vivo) |
| SCTH-002 | Backdoor in baseband modem firmware | Low | Critical | SCR-001, SCR-002 |
| SCTH-003 | Trojanized app via Play Store | Low | High | SCR-003, SCR-015 |
| SCTH-004 | Hardware implant during manufacturing | Very Low | Critical | SCR-001, SCR-007, SCR-010 |
| SCTH-005 | Compromised OTA update server | Low | Critical | SCR-001, SCR-003 |
| SCTH-006 | SIM swap attack via carrier | Low-Moderate | High | SCR-004 |
| SCTH-007 | TEE vulnerability exploitation | Very Low | Critical | SCR-005 |
| SCTH-008 | App supply chain attack (dependency) | Moderate | Moderate | SCR-011, SCR-012, SCR-015 |
| SCTH-009 | Counterfeit replacement parts (repair) | Low | Moderate | SCR-001 |
| SCTH-010 | Chinese intelligence law compulsion | Moderate (legal) | Critical | SCR-001 |

---

## 5. SUPPLY CHAIN RISK MITIGATIONS

### 5.1 Pre-Deployment Mitigations

| # | Mitigation | Status | Effectiveness |
|---|---|---|---|
| SCRM-001 | Verify device authenticity (IMEI check, sealed box) | Completed | Moderate |
| SCRM-002 | Verify Verified Boot state = green | Verified (green) | High |
| SCRM-003 | Confirm bootloader locked | Verified (locked) | High |
| SCRM-004 | Verify SELinux Enforcing | Verified (Enforcing) | High |
| SCRM-005 | Audit pre-installed packages (270 system) | Completed (ADB scan) | Moderate |
| SCRM-006 | Verify encryption active | Verified (File-Based) | High |

### 5.2 Operational Mitigations

| # | Mitigation | Status | Frequency |
|---|---|---|---|
| SCRM-007 | Monitor network traffic for anomalous connections | Planned | Continuous |
| SCRM-008 | Install apps only from Play Store | Active | Ongoing |
| SCRM-009 | Enable Play Protect scanning | Active | Continuous |
| SCRM-010 | Apply security patches promptly | Partially met (patch lag) | Monthly |
| SCRM-011 | Review app permissions | Active | Quarterly |
| SCRM-012 | Implement VPN | Planned (POA&M-CRIT-002) | Once configured |
| SCRM-013 | Monitor for unexpected battery drain | Active | Daily |
| SCRM-014 | Monitor for unexpected data usage | Active | Weekly |

### 5.3 Long-term Strategic Mitigations

| # | Mitigation | Timeline | Priority |
|---|---|---|---|
| SCRM-015 | Evaluate migration to US/Allied OEM device | 12 months | Moderate (for High baseline) |
| SCRM-016 | Implement network-level traffic analysis | 6 months | High |
| SCRM-017 | Establish approved hardware list (AHL) | 6 months | Moderate |
| SCRM-018 | Subscribe to vendor security advisories | Immediate | High |
| SCRM-019 | Document and monitor all firmware versions | Ongoing | Moderate |

---

## 6. SUPPLIER MONITORING

### 6.1 Monitoring Activities

| Supplier | Monitoring Activity | Frequency | Source |
|---|---|---|---|
| Vivo | Security advisory monitoring | Monthly | vivo.com/en/security |
| Vivo | Geopolitical risk assessment | Quarterly | CISA, Commerce Dept |
| MediaTek | CVE monitoring for MT6833 | Monthly | NVD, MediaTek advisories |
| Google | Android security bulletin review | Monthly | source.android.com/security |
| Jio | Service reliability monitoring | Continuous | Personal experience |
| All apps | Play Protect scan results | Continuous | Google Play Protect |
| All | CISA Known Exploited Vulnerabilities | Weekly | cisa.gov/known-exploited-vulnerabilities |

### 6.2 Supply Chain Incident Response

| Step | Action | Timeline |
|---|---|---|
| 1 | Identify supply chain compromise (CVE, advisory, news) | Detection |
| 2 | Assess applicability to VTMIS | Within 4 hours |
| 3 | Determine severity and impact | Within 4 hours |
| 4 | Implement immediate mitigations (isolate, disable, block) | Within 24 hours |
| 5 | Apply patches/updates when available | Per CVSS (Critical: 7 days) |
| 6 | Document in POA&M if remediation delayed | Within 48 hours |
| 7 | Update SCRM Plan with lessons learned | Within 30 days |

---

## 7. PROVENANCE TRACKING

### 7.1 Hardware Provenance

| Component | Identifier | Origin | Verification |
|---|---|---|---|
| Device | Serial: 10BDC81HTA000Z4 | Vivo factory (China) | IMEI verified |
| SoC | MediaTek MT6833 | MediaTek (Taiwan) / fab (various) | `ro.hardware` confirmed |
| Build | PD2230KF_EX_A_15.2.13.1.W30 | Vivo OTA | `ro.build.display.id` |
| Bootloader | Locked | Vivo factory | `ro.boot.verifiedbootstate` = green |
| Baseband | Unknown version | MediaTek/Vivo | `gsm.version.baseband` |
| SIM | Jio 4G | Reliance Jio (India) | Active subscriber |

### 7.2 Software Provenance

| Software | Version | Publisher | Distribution Channel | Signature Verified |
|---|---|---|---|---|
| Android 15 | API 35 | Google (AOSP) | Vivo OTA | Verified Boot |
| FuntouchOS 15 | 15.2.13.1 | Vivo | Vivo OTA | Verified Boot |
| Google Play Services | Latest | Google | Play Store | Play Store signing |
| User Apps (38) | Various | Various | Google Play Store | Play Store signing |
| Linux Kernel | 4.19.236+ | Vivo (modified) | OTA | Verified Boot |
| SELinux Policy | Android 15 | Google + Vivo | OTA | dm-verity |

---

## 8. REGULATORY COMPLIANCE

### 8.1 Applicable Regulations

| Regulation | Applicability | Compliance Status |
|---|---|---|
| NIST SP 800-161 Rev. 1 | SCRM framework | This document |
| Executive Order 14028 (Cybersecurity) | Software supply chain | Partially addressed |
| NDAA Section 889 | Prohibited entities | Vivo NOT on prohibited list (as of 2026) |
| CISA Supply Chain Guidance | Federal systems | Referenced |
| FedRAMP SCRM Requirements | Cloud services | Google GMS FedRAMP authorized |
| Indian IT Act 2000 | Carrier/ISP | Jio compliant |

### 8.2 Prohibited Components Check

| Prohibited Manufacturer (NDAA §889) | Present in VTMIS? | Status |
|---|---|---|
| Huawei Technologies | NO | Clear |
| ZTE Corporation | NO | Clear |
| Hytera Communications | NO | Clear |
| Hangzhou Hikvision | NO | Clear |
| Dahua Technology | NO | Clear |
| Kaspersky Lab | NO | Clear |

**Note:** Vivo (BBK Electronics) is NOT currently on the NDAA §889 prohibited list but shares similar country-of-origin risk characteristics. Continuous monitoring is required.

---

## 9. REVIEW AND UPDATE

| Activity | Frequency | Responsible |
|---|---|---|
| Full SCRM Plan review | Annually | AAKASH |
| Supplier risk re-assessment | Semi-annually | AAKASH |
| Geopolitical risk monitoring | Quarterly | AAKASH |
| CVE/advisory monitoring | Monthly | AAKASH |
| NDAA prohibited list check | Semi-annually | AAKASH |
| Post-incident SCRM update | After any supply chain incident | AAKASH |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Supply Chain Risk Management Plan |

---

*END OF SUPPLY CHAIN RISK MANAGEMENT PLAN*

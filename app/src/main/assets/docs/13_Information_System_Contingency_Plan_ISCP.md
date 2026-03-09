# INFORMATION SYSTEM CONTINGENCY PLAN (ISCP)

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Plan Activation Authority** | AAKASH (System Owner) |
| **Regulatory Basis** | NIST SP 800-34 Rev. 1; NIST SP 800-53 Rev. 5: CP Family |

---

## 1. INTRODUCTION

### 1.1 Purpose

This Information System Contingency Plan (ISCP) establishes procedures to recover the Vivo T2X 5G Mobile Information System (VTMIS) following a disruption. The plan provides a comprehensive framework for restoring the system from various failure scenarios, ensuring continuity of mission-critical operations.

### 1.2 Applicability

This ISCP applies to all components of the VTMIS:
- Vivo T2X 5G handset (Model V2312, Serial: 10BDC81HTA000Z4)
- Android 15 operating system with FuntouchOS 15
- All 308 installed packages (270 system + 38 user-installed)
- User data (contacts, messages, media, documents, credentials)
- Network connectivity (Jio cellular, Wi-Fi, hotspot)
- Connected peripheral (Windows 11 workstation via USB/hotspot)

### 1.3 Assumptions

1. A current backup exists (Google Cloud and/or ADB local)
2. Replacement hardware can be procured within 24-72 hours
3. AAKASH has access to Google account credentials for restoration
4. Jio carrier can transfer service to replacement device within 4 hours
5. Internet connectivity via alternate means is available during recovery

---

## 2. CONCEPT OF OPERATIONS

### 2.1 System Classification

| Parameter | Value |
|---|---|
| FIPS 199 Impact Level | Low / Moderate / High (all three addressed) |
| Mission Criticality | Moderate |
| Recovery Time Objective (RTO) | 4 hours (operational) / 24 hours (full restoration) |
| Recovery Point Objective (RPO) | 24 hours (last backup) |
| Maximum Tolerable Downtime (MTD) | 24 hours |

### 2.2 System Architecture for Recovery

```
┌─────────────────────────────────────────────────────────┐
│                    VTMIS RECOVERY ARCHITECTURE           │
│                                                          │
│  ┌──────────────┐    ┌──────────────┐    ┌────────────┐ │
│  │ PRIMARY DEVICE│    │ GOOGLE CLOUD │    │  LOCAL      │ │
│  │ Vivo T2X 5G  │◄──►│ BACKUP       │    │  BACKUP    │ │
│  │ (V2312)      │    │ (auto daily) │    │  (ADB/USB) │ │
│  └──────┬───────┘    └──────────────┘    └─────┬──────┘ │
│         │                                       │        │
│         ▼                                       ▼        │
│  ┌──────────────┐                    ┌──────────────┐    │
│  │ REPLACEMENT  │                    │ WORKSTATION  │    │
│  │ DEVICE       │◄───restore────────│ (Win 11)     │    │
│  │ (if needed)  │                    │ ADB + backup │    │
│  └──────────────┘                    └──────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 2.3 Line of Succession

| Priority | Role | Individual | Contact |
|---|---|---|---|
| 1 | System Owner / ISCP Activation Authority | AAKASH | Primary — direct |
| 2 | Alternate Recovery Operator | [Designated Alternate TBD] | [Contact TBD] |

---

## 3. BACKUP STRATEGY

### 3.1 Backup Methods

| Method | Type | Frequency | Data Covered | Storage Location | Retention |
|---|---|---|---|---|---|
| Google Cloud Backup | Automatic | Daily (on charge + Wi-Fi) | Apps, settings, call history, contacts, SMS | Google servers (encrypted) | Until manual deletion |
| Google Photos | Automatic | As captured | Photos, videos | Google servers | Per storage quota |
| ADB Full Backup | Manual | Weekly (recommended) | App data, APKs, shared storage | Workstation local disk | 4 most recent |
| Contact Export | Manual | Monthly | All contacts (VCF) | Workstation + cloud | Indefinite |
| App-Specific Sync | Automatic | Continuous | App data (WhatsApp, Telegram) | Respective cloud services | Per app policy |

### 3.2 Backup Procedures

#### 3.2.1 Google Cloud Backup (Automatic)

**Verification Command:**
```bash
adb shell bmgr list transports
adb shell bmgr enabled
```

**Settings Path:** Settings > System > Backup > Google Backup ON

**Data Included:**
- App data (for apps supporting backup)
- Call history
- Device settings
- SMS messages
- Contacts (via Google Contacts sync)
- Wi-Fi passwords
- Home screen wallpaper/layout

#### 3.2.2 ADB Full Backup (Manual)

**Procedure:**
```bash
# Ensure USB Debugging is enabled (temporarily)
adb devices  # Verify connection

# Full backup (apps + data + shared storage)
adb backup -apk -shared -all -f vtmis_backup_%date%.ab

# Verify backup file created
dir vtmis_backup_*.ab

# Disable USB Debugging after backup
```

**Backup file size estimate:** 50-100 GB (given 97% storage utilization of 106 GB)

#### 3.2.3 Contact Export

```bash
# Export contacts via ADB
adb shell content query --uri content://contacts/phones/ > contacts_export.txt

# Or use Google Contacts web export (contacts.google.com > Export > VCF)
```

### 3.3 Backup Verification

| Test | Frequency | Procedure |
|---|---|---|
| Verify Google backup status | Weekly | Check Settings > System > Backup |
| Verify ADB backup file integrity | After each backup | Check file size > 0, attempt `adb restore` on test |
| Test contact restoration | Quarterly | Import VCF to test device |
| Verify photos in Google Photos | Monthly | Check most recent photos visible in cloud |

---

## 4. CONTINGENCY SCENARIOS AND RECOVERY PROCEDURES

### 4.1 SCENARIO 1: Device Loss or Theft

**Trigger:** Device cannot be physically located; suspected stolen

| Phase | Action | Timeline | Responsible |
|---|---|---|---|
| **Immediate** | Report loss; attempt Google Find My Device locate | 0-15 min | AAKASH |
| **Assessment** | If not recoverable, initiate remote wipe | 15-30 min | AAKASH |
| **Containment** | Change Google password, change all app passwords | 30-60 min | AAKASH |
| **Recovery** | Procure replacement device | 24-72 hrs | AAKASH |
| **Restoration** | Restore from Google Cloud backup | 2-4 hrs after procurement | AAKASH |
| **Verification** | Verify all data restored, test functionality | 1-2 hrs | AAKASH |
| **Documentation** | File incident report, update ISCP lessons learned | 24 hrs | AAKASH |

**Detailed Recovery Steps:**
1. Go to [google.com/android/find](https://google.com/android/find)
2. Sign in with Google account
3. Select "Secure Device" to lock with message
4. If unrecoverable, select "Erase Device"
5. Report to law enforcement if stolen (file FIR in India)
6. Contact Jio to suspend SIM / block IMEI
7. Procure replacement Vivo T2X 5G (or compatible Android device)
8. Insert new/replacement SIM from Jio
9. Set up device with same Google account
10. Select "Restore from cloud backup"
11. Re-install apps not covered by backup
12. Verify contacts, messages, settings restored
13. Re-enroll biometrics
14. Apply all security configuration baselines per CM Plan

---

### 4.2 SCENARIO 2: Hardware Failure

**Trigger:** Device non-functional (screen failure, board failure, water damage, etc.)

| Phase | Action | Timeline |
|---|---|---|
| **Assessment** | Determine if device is repairable | 0-4 hrs |
| **Decision Point** | Repair vs. replace | 4 hrs |
| **If Repairable** | Send to Vivo service center | 3-7 days |
| **If Not Repairable** | Procure replacement | 24-72 hrs |
| **Data Extraction** | Attempt ADB data pull if USB functions | Before disposal |
| **Restoration** | Restore from most recent backup | 2-4 hrs |
| **Secure Disposal** | Wipe failed device before disposal/return | Before release |

**Data Extraction Attempt (if device partially functional):**
```bash
# Check if ADB still connects
adb devices

# If connected, emergency backup
adb backup -apk -shared -all -f emergency_backup.ab

# If ADB unavailable but device boots
# Try Google Cloud backup (connect to Wi-Fi/cellular)
```

---

### 4.3 SCENARIO 3: Operating System Corruption

**Trigger:** Device bootloop, system app crashes, unresponsive OS

| Phase | Action | Timeline |
|---|---|---|
| **Tier 1** | Force restart (hold power 15 sec) | 0-5 min |
| **Tier 2** | Boot to Safe Mode; uninstall recent apps | 5-30 min |
| **Tier 3** | Clear app cache (Recovery mode > Wipe Cache) | 30-60 min |
| **Tier 4** | Factory reset + restore from backup | 2-4 hrs |

**Safe Mode Boot:**
1. Power off device
2. Hold Power button to boot
3. When Vivo logo appears, hold Volume Down
4. "Safe Mode" appears at bottom-left
5. Uninstall recently installed apps
6. Restart normally

**Factory Reset (Last Resort):**
1. Backup data if possible (ADB or Google Cloud)
2. Settings > System > Reset > Factory data reset
3. Confirm and enter PIN
4. Wait for reset to complete (10-20 minutes)
5. Set up device with Google account
6. Restore from cloud backup
7. Re-apply security baselines

---

### 4.4 SCENARIO 4: Ransomware/Malware Infection

**Trigger:** Suspicious encryption, unusual behavior, ransom demand, data exfiltration detected

| Phase | Action | Timeline |
|---|---|---|
| **Isolation** | Enable Airplane Mode immediately | 0-1 min |
| **Assessment** | Boot to Safe Mode; identify malicious app | 1-15 min |
| **Containment** | Uninstall malicious app via Safe Mode | 15-30 min |
| **Eradication** | Run Play Protect full scan; if needed, factory reset | 30 min-2 hrs |
| **Recovery** | Restore from pre-infection backup | 2-4 hrs |
| **Post-Incident** | Update IOCs; report per IRP | 24 hrs |

**CRITICAL:** Do NOT connect device to workstation until malware is eradicated.

**Evidence Preservation (if ADB available):**
```bash
adb shell dumpsys package [suspicious_package]
adb shell pm list packages -3  # Check for unknown apps
adb logcat -d > malware_evidence_log.txt
adb bugreport > bugreport_evidence.zip
```

---

### 4.5 SCENARIO 5: Cellular Network Disruption

**Trigger:** Jio network outage, SIM failure, tower maintenance

| Phase | Action | Timeline |
|---|---|---|
| **Detection** | No cellular signal; verify airplane mode is off | 0-5 min |
| **Workaround** | Connect to Wi-Fi for internet access | 5-10 min |
| **If SIM failure** | Restart device; reseat SIM | 5-10 min |
| **If prolonged** | Contact Jio support; obtain replacement SIM | 4-24 hrs |
| **Alternative** | Use workstation directly for Federal data access | Immediate |

---

### 4.6 SCENARIO 6: Google Account Compromise

**Trigger:** Unauthorized login detected, suspicious activity alert

| Phase | Action | Timeline |
|---|---|---|
| **Immediate** | Change Google password from secure device | 0-5 min |
| **Containment** | Review and revoke unauthorized sessions | 5-15 min |
| **Assessment** | Check Google backup integrity | 15-30 min |
| **Recovery** | Re-secure account (MFA, recovery options) | 30-60 min |
| **Verification** | Verify no unauthorized data access | 1-2 hrs |

---

## 5. RECOVERY PRIORITY

### 5.1 Critical Functions (Priority 1 — Restore within RTO)

| Function | Recovery Method | RTO |
|---|---|---|
| Cellular voice/data connectivity | SIM transfer to replacement device | 4 hours |
| Device encryption | Android FBE enabled by default | On setup |
| PIN/biometric lock | Configured during setup | On setup |
| Google account sync | Sign-in during setup | 30 min |

### 5.2 Essential Functions (Priority 2 — Restore within 24 hours)

| Function | Recovery Method | Timeline |
|---|---|---|
| Contacts and call logs | Google Cloud backup restore | 1 hour |
| SMS/MMS messages | Google Cloud backup restore | 1 hour |
| Hotspot for workstation | Configure after setup | 30 min |
| Email access | Google account restore | 30 min |

### 5.3 Supporting Functions (Priority 3 — Restore within 72 hours)

| Function | Recovery Method | Timeline |
|---|---|---|
| All 38 user-installed apps | Play Store re-download | 2-4 hours |
| App data/preferences | Google backup + app-specific restore | 4-8 hours |
| Photos/videos | Google Photos restore | 4-24 hours |
| Security baseline configuration | Manual configuration per CM Plan (Doc 07) | 2 hours |
| VPN configuration | Re-configure per ISA-004 | 1 hour |

---

## 6. PLAN TESTING

### 6.1 Test Schedule

| Test Type | Frequency | Description |
|---|---|---|
| Tabletop Exercise | Annually | Walk through each scenario verbally |
| Backup Verification | Quarterly | Confirm backup data integrity and accessibility |
| Partial Restoration Test | Semi-annually | Restore contacts/app data to test device |
| Full Recovery Test | Annually | Full factory reset and cloud restoration |
| Communication Test | Semi-annually | Verify contact information for carrier, Google, Vivo |

### 6.2 Test Results Template

| Field | Entry |
|---|---|
| Test Date | |
| Test Type | |
| Scenario Tested | |
| Duration | |
| Success Criteria Met? | YES / NO |
| Issues Identified | |
| Corrective Actions | |
| Tester | |
| Next Test Date | |

---

## 7. PLAN MAINTENANCE

| Activity | Frequency | Trigger |
|---|---|---|
| Full ISCP review | Annually | Calendar |
| Update contact information | Semi-annually | Calendar |
| Update after hardware change | As needed | Device replacement |
| Update after OS upgrade | As needed | Android version change |
| Update after app inventory change | Quarterly | New app install/remove |
| Post-incident update | After any contingency activation | Incident completion |

---

## 8. APPENDIX: EMERGENCY CONTACT INFORMATION

| Contact | Purpose | Number/Info |
|---|---|---|
| Jio Customer Care | SIM replacement, network issues | 199 (from Jio) / 1800-889-9999 |
| Vivo India Service | Hardware repair/replacement | 1800-102-8486 |
| Google Account Recovery | Account compromise | accounts.google.com/recovery |
| Cyber Crime India | Cybercrime reporting | cybercrime.gov.in / 1930 |
| Local Police | Theft reporting | 100 |
| Google Find My Device | Remote lock/wipe | google.com/android/find |

---

## 9. APPENDIX: RECOVERY CHECKLIST

### Post-Recovery Verification Checklist

- [ ] Device powers on and boots successfully
- [ ] Encryption confirmed active (`adb shell getprop ro.crypto.state` = encrypted)
- [ ] SELinux confirmed enforcing (`adb shell getenforce` = Enforcing)
- [ ] PIN lock configured (≥ 6 digits)
- [ ] Biometric enrolled (fingerprint)
- [ ] Google account signed in and syncing
- [ ] Contacts restored and verified
- [ ] Jio cellular connected (LTE/5G)
- [ ] Hotspot configured with WPA2/3 and strong passphrase
- [ ] Play Protect enabled and updated
- [ ] All MANDATORY apps installed and functional
- [ ] Security patches current (or latest available)
- [ ] Developer Options DISABLED
- [ ] USB Debugging DISABLED
- [ ] Auto-backup verified and scheduled
- [ ] VPN configured (if available)
- [ ] Incident documented (if recovery was due to incident)

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Information System Contingency Plan |

---

*END OF INFORMATION SYSTEM CONTINGENCY PLAN*

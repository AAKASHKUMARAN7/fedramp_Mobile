# CONTINGENCY PLAN (CP)

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |

---

## 1. INTRODUCTION

This Contingency Plan (CP) establishes procedures for restoring the VTMIS following a disruption. Developed per NIST SP 800-34 Rev. 1, "Contingency Planning Guide for Federal Information Systems," this plan ensures Federal information processed on the VTMIS can be recovered within acceptable timeframes.

---

## 2. SYSTEM OVERVIEW

| Attribute | Value |
|---|---|
| **Device** | Vivo T2X 5G (V2312), Serial: 10BDC81HTA000Z4 |
| **OS** | Android 15 / FuntouchOS 15 |
| **Storage** | 128 GB (File-Based Encrypted) |
| **Primary Network** | Jio LTE/5G NR |
| **Recovery Objectives** | RTO: 4 hours, RPO: 24 hours |

---

## 3. RECOVERY OBJECTIVES

| Objective | Value | Justification |
|---|---|---|
| **Recovery Time Objective (RTO)** | 4 hours | Maximum acceptable downtime for Federal processing |
| **Recovery Point Objective (RPO)** | 24 hours | Maximum acceptable data loss (daily backup cycle) |
| **Maximum Tolerable Downtime (MTD)** | 24 hours | Business impact threshold |

---

## 4. CONTINGENCY SCENARIOS

### 4.1 Scenario: Device Loss or Theft

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Detection** | Device missing from possession | AAKASH | Immediate |
| **Notification** | Report to AO and Law Enforcement | AAKASH | Within 1 hour |
| **Containment** | Remote lock via Google Find My Device | AAKASH | Within 15 minutes |
| **Eradication** | Remote wipe via Google Find My Device | AAKASH | Within 1 hour |
| **Recovery** | Procure replacement device | AAKASH | Within 24 hours |
| **Restoration** | Restore from Google Cloud Backup | AAKASH | Within 4 hours of new device |

### 4.2 Scenario: Hardware Failure

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Detection** | Device non-functional (screen, battery, board) | AAKASH | Immediate |
| **Assessment** | Determine if repairable or replacement needed | AAKASH | Within 2 hours |
| **Containment** | Secure failed device (data still encrypted) | AAKASH | Immediate |
| **Recovery (Repair)** | Send to Vivo authorized service center | AAKASH | 3-7 business days |
| **Recovery (Replace)** | Procure new Vivo T2X 5G or equivalent | AAKASH | Within 24 hours |
| **Restoration** | Restore from backup to repaired/new device | AAKASH | Within 4 hours |

### 4.3 Scenario: Software/OS Corruption

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Detection** | Bootloop, crashes, unresponsive system | AAKASH | Immediate |
| **Diagnosis** | Attempt safe mode boot (Power + Volume Down) | AAKASH | Within 30 minutes |
| **Recovery (Mild)** | Clear cache partition via recovery mode | AAKASH | Within 1 hour |
| **Recovery (Severe)** | Factory reset from recovery mode | AAKASH | Within 2 hours |
| **Restoration** | Restore from Google Cloud Backup | AAKASH | Within 4 hours |
| **Verification** | Verify all security configurations | AAKASH | Within 1 hour post-restore |

### 4.4 Scenario: Data Corruption/Ransomware

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Detection** | Files inaccessible, ransom message, encryption alert | AAKASH | Immediate |
| **Containment** | Enable Airplane Mode immediately | AAKASH | Within 1 minute |
| **Analysis** | Determine scope via ADB from workstation | AAKASH | Within 1 hour |
| **Eradication** | Factory reset (full wipe) | AAKASH | Within 2 hours |
| **Recovery** | Restore from last known-good backup | AAKASH | Within 4 hours |
| **Reporting** | Report incident per IRP | AAKASH | Within 1 hour of detection |

### 4.5 Scenario: Network Disruption (Jio Outage)

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Detection** | No cellular connectivity | AAKASH | Within minutes |
| **Workaround** | Connect to available Wi-Fi network | AAKASH | Within 15 minutes |
| **Alternative** | Use alternate SIM card (if available) | AAKASH | Within 1 hour |
| **Documentation** | Document outage duration and impact | AAKASH | Post-resolution |

### 4.6 Scenario: Natural Disaster

| Phase | Action | Responsible | Timeline |
|---|---|---|---|
| **Preparation** | Ensure backup is current before known events | AAKASH | Pre-event |
| **During** | Protect device physically, ensure charged | AAKASH | During event |
| **After** | Assess device damage, verify functionality | AAKASH | Post-event |
| **Recovery** | Execute applicable scenario (loss, damage) | AAKASH | Per scenario |

---

## 5. BACKUP STRATEGY

### 5.1 Backup Methods

| Method | Type | Frequency | Data Covered | Location |
|---|---|---|---|---|
| **Google Cloud Backup** | Automatic | Daily (when on Wi-Fi/charging) | Apps, settings, call history, SMS | Google Cloud (encrypted) |
| **Google Photos** | Automatic | Continuous | Photos and videos | Google Cloud |
| **Workstation Backup** | Manual | Weekly | Full device (ADB backup) | C:\Users\aakas\Backups\VTMIS\ |
| **App-specific** | Automatic | Varies | WhatsApp, Telegram chat backups | Respective cloud services |

### 5.2 Backup Verification

| Check | Frequency | Method |
|---|---|---|
| Cloud backup status | Weekly | Settings > System > Backup |
| Last backup date | Weekly | Must be within 24 hours |
| Workstation backup integrity | Monthly | Test restore to verify |
| App backup coverage | Monthly | Review backed-up app list |

### 5.3 ADB Manual Backup Procedure

```bash
# Full backup (encrypted with user password)
adb backup -apk -shared -all -system -f VTMIS_backup_$(date +%Y%m%d).adb

# Verify backup file size (should be > 1GB for full backup)
dir VTMIS_backup_*.adb

# Store backup in secure, encrypted location on workstation
```

---

## 6. RESTORATION PROCEDURES

### 6.1 New Device Setup Procedure

1. **Procure replacement device** (Vivo T2X 5G or equivalent Android 15+ device)
2. **Initial setup:** Power on, select language, connect to Wi-Fi
3. **Sign in** with Google account during setup
4. **Select "Restore from Google backup"** when prompted
5. **Wait for restoration** (apps, settings, data — may take 1-2 hours)
6. **Apply security configurations:**
   - Set PIN lock
   - Enroll biometrics (fingerprint + face)
   - Verify encryption is enabled
   - Verify SELinux is Enforcing
   - Configure lock screen banner
   - Install and configure VPN (when available)
7. **Verify all apps** are restored and functional
8. **Run ADB security scan** to establish new baseline
9. **Update SSP** with new device serial number and any configuration changes
10. **Notify AO** of device replacement and updated system posture

### 6.2 ADB Restore Procedure

```bash
# Restore from full backup
adb restore VTMIS_backup_YYYYMMDD.adb

# User will be prompted on device to confirm and enter backup password
```

---

## 7. PLAN TESTING

| Test Type | Frequency | Description |
|---|---|---|
| **Tabletop Exercise** | Quarterly | Walk through scenarios verbally |
| **Backup Verification** | Monthly | Verify backup existence and recency |
| **Partial Recovery Test** | Semi-annually | Restore single app from backup |
| **Full Recovery Test** | Annually | Full factory reset and restore |
| **Google Find My Device Test** | Annually | Test remote locate and lock (not wipe) |

---

## 8. PLAN MAINTENANCE

| Activity | Frequency | Responsible |
|---|---|---|
| Review and update CP | Annually or after significant change | AAKASH |
| Update contact information | Quarterly | AAKASH |
| Review backup strategy | Semi-annually | AAKASH |
| Post-incident CP review | After each contingency event | AAKASH |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Contingency Plan |

---

*END OF CONTINGENCY PLAN*

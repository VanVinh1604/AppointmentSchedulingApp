# 🧪 MoMo Payment Flow - Testing Guide

## ✅ Các thay đổi đã triển khai

### 1. AndroidManifest.xml
- ✅ Thêm intent-filter cho `appointmentschedulingapp://payment` scheme
- ✅ Giúp app nhận callback từ MoMo

### 2. MomoPaymentProcessor.kt
- ✅ Cập nhật returnurl: `appointmentschedulingapp://payment?orderId=$bookingId`

### 3. BookingEvent.kt
- ✅ Thêm `PaymentFailure` event

### 4. BookingViewModel.kt
- ✅ Xử lý `PaymentFailure` event

### 5. BookingNavGraph.kt
- ✅ Deep link: `appointmentschedulingapp://payment?orderId={orderId}`
- ✅ LaunchedEffect bắt `orderId` → gọi `PaymentSuccess`
- ✅ LaunchedEffect kích hoạt điều hướng sang `BookingReceiptScreen`

### 6. BookingStep4Screen.kt (đã có sẵn)
- ✅ Loading overlay khi `isLoading = true`
- ✅ Auto-open MoMo URL khi `momoPayUrl` thay đổi
- ✅ Auto-navigate khi `isSuccess = true`

---

## 🧪 Test Scenarios

### **Scenario 1: Thanh toán MoMo thành công**

**Bước thực hiện:**
1. Điền đầy đủ thông tin lịch khám (clinic, bác sĩ, ngày giờ, hồ sơ bệnh nhân)
2. Chọn phương thức **"MoMo"** trên BookingStep4
3. Bấm nút **"THANH TOÁN NGAY"**

**Kết quả dự kiến:**
- ✅ Loading overlay hiển thị "Đang xử lý giao dịch..."
- ✅ MoMo app mở ra (hoặc popup nếu MoMo chưa cài)
- ✅ Xác nhận thanh toán trên MoMo
- ✅ App tự động chuyển sang **BookingReceiptScreen**
- ✅ Hiển thị thông tin xác nhận đơn lịch khám (booking ID, QR code, v.v.)

**Logs cần kiểm tra:**
```
BookingViewModel: PaymentSuccess event received
BookingNavGraph: orderId=<booking_id> received from deep link
Navigation: Navigate to BookingReceiptScreen
```

---

### **Scenario 2: Thanh toán Tiền mặt**

**Bước thực hiện:**
1. Điền đầy đủ thông tin lịch khám
2. Chọn phương thức **"Tiền mặt"** trên BookingStep4
3. Bấm nút **"THANH TOÁN NGAY"**

**Kết quả dự kiến:**
- ✅ Không mở app khác
- ✅ Không có loading overlay (vì `isLoading` được tắt ngay)
- ✅ App tự động chuyển sang **BookingReceiptScreen**
- ✅ Hiển thị thông tin xác nhận

**Logs cần kiểm tra:**
```
CashPaymentProcessor: Payment processed
BookingViewModel: PaymentSuccess (direct)
Navigation: Navigate to BookingReceiptScreen
```

---

### **Scenario 3: User hủy MoMo (back button)**

**Bước thực hiện:**
1. Chọn MoMo, bấm thanh toán
2. MoMo app mở
3. Bấm back hoặc close MoMo mà **chưa xác nhận thanh toán**

**Kết quả dự kiến:**
- ✅ Quay lại BookingStep4Screen
- ✅ Loading overlay biến mất
- ✅ User có thể chọn lại phương thức hoặc thử lại MoMo
- ❌ KHÔNG tự động chuyển sang Receipt (vì không nhận được orderId)

**Lý do:** Deep link callback chỉ được gửi khi MoMo xác nhận thành công. Nếu user bấm back, không có callback.

---

### **Scenario 4: Kiểm tra Deep Link (Advanced)**

**Bước thực hiện (Debug):**
1. Mở terminal và chạy:
```bash
adb shell am start -W -a android.intent.action.VIEW \
  -d "appointmentschedulingapp://payment?orderId=test_booking_123" \
  com.example.appointmentschedulingapp
```

**Kết quả dự kiến:**
- ✅ App nhận được deep link
- ✅ BookingNavGraph xử lý `orderId=test_booking_123`
- ✅ Gọi `BookingEvent.PaymentSuccess`
- ✅ Điều hướng sang BookingReceiptScreen

---

## 📊 Flow Diagram

```
┌─────────────────┐
│ BookingStep4    │
│ (chọn thanh toán│
└────────┬────────┘
         │
         ├─ MoMo: isLoading = true
         │         ↓
         │    MoMo app mở
         │         │
         │    [User xác nhận]
         │         │
         │    MoMo callback:
         │    appointmentschedulingapp://payment?orderId=xyz
         │         │
         ├────────→ BookingNavGraph
         │         ↓
         │    LaunchedEffect(orderId)
         │         ↓
         │    PaymentSuccess event
         │         ↓
         │    isSuccess = true
         │         ↓
         └───────→ BookingReceiptScreen ✅
```

---

## 🔍 Kiểm tra thực tế

### **Android Logcat Filters:**

```
# BookingViewModel events
adb logcat BookingViewModel:*

# Navigation events
adb logcat Navigation:*

# Deep link events
adb logcat DeepLink:*

# All relevant logs
adb logcat | grep -E "(BookingViewModel|Navigation|orderId|PaymentSuccess|BookingReceipt)"
```

### **Key Log Messages to Look For:**

✅ Thành công:
```
BookingViewModel: PaymentSuccess event
Navigation: Navigate BookingReceipt
orderId received: booking_abc123
```

❌ Lỗi:
```
ActivityNotFoundException: MoMo app not found
orderId is null (user pressed back)
Deep link not received
```

---

## ⚙️ Configuration Checklist

- [ ] AndroidManifest.xml có intent-filter cho `appointmentschedulingapp://payment`
- [ ] MomoPaymentProcessor trả về correct returnurl
- [ ] BookingNavGraph có deep link pattern: `appointmentschedulingapp://payment?orderId={orderId}`
- [ ] BookingEvent.PaymentSuccess được gọi khi orderId không null
- [ ] LaunchedEffect trong BookingStep4 kích hoạt điều hướng
- [ ] BookingReceiptScreen hiển thị thông tin booking

---

## 📱 Testing trên MoMo Sandbox

### **MoMo Test Account:**
- App: `vn.momo.platform.test` (MoMo Sandbox)
- Tài khoản test: Liên hệ MoMo support

### **Test Cards:**
- **Thành công:** Áp dụng các thẻ test của MoMo
- **Thất bại:** Các thẻ kết thúc bằng số lẻ thường thất bại

### **MoMo Sandbox Dashboard:**
- Truy cập: https://mfc-dev.momoapp.vn (nếu có quyền)
- Kiểm tra transaction logs
- Xác nhận callback status

---

## 🐛 Debugging Tips

### **Deep Link không được nhận:**
1. Kiểm tra intent-filter trong AndroidManifest
2. Xác nhận scheme và host đúng
3. Logs: `adb logcat | grep -i intent`

### **Loading mãi không tắt:**
1. Kiểm tra orderId có được pass vào BookingStep4 không
2. Logs: `adb logcat BookingViewModel:*`
3. Verify LaunchedEffect(orderId) được kích hoạt

### **Không điều hướng sang Receipt:**
1. Kiểm tra isSuccess có thay đổi không
2. Logs: `adb logcat Navigation:*`
3. Verify LaunchedEffect(uiState.isSuccess) được kích hoạt

### **MoMo app không mở:**
1. Kiểm tra MoMo app có cài đặt không
2. Kiểm tra deep link URI format
3. Logs: `adb logcat ActivityNotFoundException`

---

## ✅ Final Verification

Trước khi release, đảm bảo:

- [ ] ✅ Thanh toán MoMo → tự động sang Receipt
- [ ] ✅ Thanh toán Tiền mặt → tự động sang Receipt  
- [ ] ✅ User cancel MoMo → quay lại Step4 (không crash)
- [ ] ✅ Loading state tắt đúng lúc
- [ ] ✅ Booking ID hiển thị chính xác trên Receipt
- [ ] ✅ Không có memory leaks
- [ ] ✅ Deep link handling không bị crash

---

## 📞 Support

Nếu có vấn đề:
1. Kiểm tra logs theo hướng dẫn trên
2. Kiểm tra các files đã được cập nhật đúng
3. Rebuild project: `Build → Clean Project → Rebuild Project`
4. Clear app cache: `Settings → Apps → [Your App] → Storage → Clear Cache`

**Good luck with your MoMo payment integration! 🚀**


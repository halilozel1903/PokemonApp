# CI/CD Pipeline Documentation

Bu doküman, projeye eklenen CI/CD (Sürekli Entegrasyon/Sürekli Dağıtım) pipeline'ı açıklar.

## Genel Bakış

Projeye iki GitHub Actions workflow'u eklenmiştir:

1. **Android CI** (`android-ci.yml`) - Ana build ve test workflow'u
2. **PR Build Check** (`pr-check.yml`) - Pull Request'ler için özel kontrol workflow'u

## Workflow'ların Ne Zaman Çalıştığı

### Android CI
- `main` veya `master` branch'ine her push yapıldığında
- `main` veya `master` branch'ine açılan her Pull Request'te
- Manuel olarak GitHub Actions sekmesinden

### PR Build Check
- Yeni bir Pull Request açıldığında
- Pull Request güncellendiğinde (yeni commit eklendiğinde)
- Pull Request yeniden açıldığında

## Yapılan Kontroller

Her iki workflow da şu adımları gerçekleştirir:

1. ✅ **Kod Checkout**: Repository kodu indirilir
2. ☕ **Java Kurulumu**: JDK 17 kurulur ve Gradle cache aktif edilir
3. 🔨 **Build**: Proje Gradle ile derlenir
4. 🧪 **Unit Testler**: Tüm birim testler çalıştırılır
5. 🔍 **Lint Kontrolü**: Kod kalitesi ve stil kontrolleri yapılır
6. 📦 **Artifact Upload**: Başarılı build'lerde APK, başarısız build'lerde raporlar yüklenir

## PR Build Check'in Özel Özellikleri

PR Build Check workflow'u ek olarak:
- Pull Request'e otomatik yorum ekler (✅ başarılı / ❌ başarısız)
- Lint raporlarını artifact olarak kaydeder
- Daha detaylı build doğrulaması yapar

## Build Durumunu Kontrol Etme

1. GitHub repository'ye gidin
2. "Actions" sekmesine tıklayın
3. Son workflow çalışmalarını göreceksiniz
4. Detayları görmek için herhangi bir çalışmaya tıklayın

## Build Badge'leri

README.md dosyasının başına eklenen badge'ler, build durumunu gerçek zamanlı gösterir:

```markdown
[![Android CI](https://github.com/halilozel1903/PokemonApp/workflows/Android%20CI/badge.svg)]
```

- Yeşil: Build başarılı ✅
- Kırmızı: Build başarısız ❌
- Gri: Henüz çalışmadı veya durum belirsiz

## Merge Öncesi Kontrol

Artık bir Pull Request merge edilmeden önce:

1. Tüm workflow'ların başarıyla tamamlanması gerekir
2. Build'in başarılı olması zorunludur
3. Tüm testlerin geçmesi gerekir
4. Lint kontrollerinin hata vermemesi önerilir

Bu sayede main/master branch'ine hatalı kod merge edilmesi önlenir.

## Troubleshooting

### Build Başarısız Olursa Ne Yapmalıyım?

1. Actions sekmesinde başarısız workflow'a tıklayın
2. Hangi adımda hata oluştuğunu görün
3. Hata mesajlarını okuyun
4. Gerekli düzeltmeleri yapıp yeni commit ekleyin

### Artifact'leri Nasıl İndirebilirim?

1. Başarısız bir workflow çalışmasına gidin
2. Sayfa sonundaki "Artifacts" bölümüne bakın
3. "build-reports" veya "lint-report" dosyalarını indirin
4. Detaylı hata raporlarını inceleyin

## Yerel Olarak Test Etme

Workflow'ların yerel olarak başarılı olup olmayacağını test etmek için:

```bash
# Build
./gradlew build --no-daemon --stacktrace

# Lint kontrolü
./gradlew lint --no-daemon

# Unit testler
./gradlew test --no-daemon
```

## Katkıda Bulunanlar İçin

Yeni bir özellik eklerken veya hata düzeltirken:

1. Yeni bir branch oluşturun
2. Değişikliklerinizi yapın
3. Pull Request açın
4. CI/CD workflow'larının başarıyla tamamlanmasını bekleyin
5. Code review sonrası merge edin

## Sonuç

Bu CI/CD pipeline'ı sayede:
- ✅ Her değişiklik otomatik test edilir
- ✅ Hatalı kod merge edilmesi önlenir
- ✅ Kod kalitesi sürekli kontrol edilir
- ✅ Geliştirme süreci daha güvenli hale gelir

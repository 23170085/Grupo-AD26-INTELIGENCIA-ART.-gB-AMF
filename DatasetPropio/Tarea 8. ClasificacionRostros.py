import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
import os
import json

from tensorflow.keras import layers, models

# La carpeta Rostros está junto a este script
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(SCRIPT_DIR, "Rostros")

if not os.path.isdir(DATA_DIR):
    raise FileNotFoundError(
        f"No se encontró la carpeta de imágenes 'Rostros' en:\n{DATA_DIR}"
    )

# Configuración de las imágenes
IMG_SIZE = 128
BATCH_SIZE = 32

# Dataset de entrenamiento (80%)
train_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="training",
    seed=123,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    label_mode="categorical"
)

# Dataset de validación (20%)
val_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="validation",
    seed=123,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    label_mode="categorical"
)

# Obtener los nombres de las personas
class_names = train_ds.class_names

# Cantidad de personas que reconocerá el modelo
num_classes = len(class_names)

print("Personas detectadas:", class_names)
print("Cantidad de clases:", num_classes)

plt.figure(figsize=(10, 10))

for images, labels in train_ds.take(1):
    for i in range(min(25, len(images))):
        plt.subplot(5, 5, i + 1)

        plt.imshow(images[i].numpy().astype("uint8"))
        etiqueta = np.argmax(labels[i].numpy())

        plt.title(class_names[etiqueta])
        plt.axis("off")

plt.tight_layout()
plt.show()

base_model = tf.keras.applications.MobileNetV2(
    input_shape=(IMG_SIZE, IMG_SIZE, 3),
    include_top=False,
    weights="imagenet"
)
base_model.trainable = False

model = models.Sequential([
    layers.Input(shape=(IMG_SIZE, IMG_SIZE, 3)),
    layers.RandomFlip("horizontal"),
    layers.RandomRotation(0.08),
    layers.RandomZoom(0.10),
    layers.Rescaling(1. / 127.5, offset=-1),
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.BatchNormalization(),
    layers.Dropout(0.4),
    layers.Dense(128, activation="relu"),
    layers.Dropout(0.3),
    layers.Dense(num_classes, activation="softmax")
])

model.summary()

model.compile(
    optimizer="adam",
    loss="categorical_crossentropy",
    metrics=["accuracy"]
)

callbacks = [

    # Detiene el entrenamiento si deja de mejorar
    tf.keras.callbacks.EarlyStopping(
        monitor="val_loss",
        patience=7,
        restore_best_weights=True
    ),

    # Guarda el mejor modelo
    tf.keras.callbacks.ModelCheckpoint(
        os.path.join(SCRIPT_DIR, "mejor_modelo.keras"),
        monitor="val_accuracy",
        mode="max",
        save_best_only=True
    )
]

history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=40,
    callbacks=callbacks
)

plt.figure(figsize=(12,5))

# Gráfica de precisión
plt.subplot(1,2,1)

plt.plot(history.history["accuracy"], label="Entrenamiento")
plt.plot(history.history["val_accuracy"], label="Validación")

plt.title("Precisión del modelo")
plt.xlabel("Épocas")
plt.ylabel("Accuracy")
plt.legend()

# Gráfica de pérdida
plt.subplot(1,2,2)

plt.plot(history.history["loss"], label="Entrenamiento")
plt.plot(history.history["val_loss"], label="Validación")

plt.title("Pérdida del modelo")
plt.xlabel("Épocas")
plt.ylabel("Loss")
plt.legend()

plt.tight_layout()
plt.show()

with open(os.path.join(SCRIPT_DIR, "clases.json"), "w", encoding="utf-8") as archivo:
    json.dump(class_names, archivo, ensure_ascii=False)

print("Modelo y etiquetas guardados correctamente.")
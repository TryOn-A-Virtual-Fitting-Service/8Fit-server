import traceback

from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from keras.api.applications.mobilenet_v2 import preprocess_input
from keras.api.preprocessing import image
import tensorflow as tf
import numpy as np
from PIL import Image
import io
import time

app = FastAPI()


@app.get("/")
def health_check():
    return {"message": "image-classification-worker is running"}


# 모델 정보
MODEL_PATH = "./models/mobilenet_v2_fashion_classifier.h5"
IMAGE_SIZE = (224, 224)

# 모델 로딩
print("Loading model")
start_time = time.time()
try:
    model = tf.keras.models.load_model(MODEL_PATH)
except Exception as e:
    print(f"Error loading model: {e}")
    model = None
print(f"Model loaded in {time.time() - start_time:.2f} seconds")

class_dictionary = {
    0: 'Accessories',
    1: 'LongPants',
    2: 'LongSleeve',
    3: 'Outerwear',
    4: 'Shoes',
    5: 'ShortPants',
    6: 'ShortSleeve',
    7: 'Sleeveless'
}


# 전처리 함수
def preprocess_image(image_bytes):
    img = Image.open(io.BytesIO(image_bytes)).convert("RGB")
    img = img.resize(IMAGE_SIZE)
    img_array = image.img_to_array(img)
    img_array = preprocess_input(img_array)
    img_array = np.expand_dims(img_array, axis=0)  # 배치 차원 추가
    return img_array


# 예측
@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    if model is None:
        return JSONResponse(content={"error": "Model could not be loaded"}, status_code=500)

    try:
        print(f"Received file: {file.filename}")
        # 이미지 전처리
        image_bytes = await file.read()
        processed_image = preprocess_image(image_bytes)

        # 예측
        predictions = model.predict(processed_image)
        predicted_class_idx = np.argmax(predictions[0])
        predicted_class = class_dictionary[predicted_class_idx]
        confidence = float(predictions[0][predicted_class_idx])
        print(f"Predicted class: {predicted_class}, confidence: {confidence:.2f}")

        return JSONResponse(content={"class": predicted_class})

    except Exception as e:
        # 예외가 발생한 경우 상세한 오류 정보 반환
        error_details = traceback.format_exc()  # 전체 스택 트레이스
        return JSONResponse(content={"error": str(e), "details": error_details}, status_code=500)


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

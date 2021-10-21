
from rest_framework import status

# from maize_api.serializers import DataSerializer
from maize_api.models import Data
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
import sys 
import os

from django.core.files import File  # you need this somewhere
import urllib


# class DataViewSet(viewsets.ModelViewSet):
#    queryset = Data.objects.all()
#    serializer_class = DataSerializer
import base64
@api_view(['POST'])
def setData(request):
    try:
        data = request.data
        data_obj = Data()
        data_obj.time_stamp = data['time_stamp']
        time_stamp_str=str(data['time_stamp'])
        data_obj.latitude = data['latitude']
        data_obj.longitude = data['longitude']
        data_obj.user_id = data['user_id']
        data_obj.predicted_class = data['predicted_class']
        data_obj.crop_type = data['crop_type']

        encoded_image_str=str(data['encoded_image'])
        data_obj.encoded_image = encoded_image_str

        imgdata = base64.b64decode(encoded_image_str) 
        # filename = '{}_{}.png'.format(data['user_id'], data['time_stamp'])
        time_stamp_str=time_stamp_str=str(data['time_stamp'])
        time_stamp_str=time_stamp_str.replace('/', '-')
        time_stamp_str=time_stamp_str.replace(' ', '_')
        time_stamp_str=time_stamp_str.replace(':', '-')

        user_id_str=str(data['user_id'])
        user_id_str=user_id_str.replace('/', '-')
        user_id_str=user_id_str.replace(' ', '_')
        time_stamp_str=time_stamp_str.replace(':', '-')

        # filename='D:/Semester V/RnD/Backend/maize_detetion_backend/uploads/'+time_stamp_str+'_'+user_id_str+'.png'
        imagePath=str('D:/Semester V/RnD/Backend/maize_detetion_backend/uploads/'+time_stamp_str+'_'+user_id_str+'.png')
        # print('uploads/'+time_stamp_str+'_'+user_id_str+'.png')
        with open(imagePath, 'wb') as f:
            f.write(imgdata)
        data_obj.image_path=imagePath
        # result = urllib.urlretrieve(filename) # image_url is a URL to an image


        data_obj.save()
        return Response({"message":"Data Sent"}, status=status.HTTP_200_OK )
    except Exception:
        print(sys.exc_info())
        return Response({"message":"Bad Request - python Exception"}, status=status.HTTP_400_BAD_REQUEST )
        # return Response({"message":'uploads/'+time_stamp_str+'_'+user_id_str+'.png'}, status=status.HTTP_400_BAD_REQUEST )